package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.ProfileRequest;
import com.dokanne.DokaneeBackend.dto.response.ProfileResponse;
import com.dokanne.DokaneeBackend.jwt.dto.response.UserResponse;
import com.dokanne.DokaneeBackend.jwt.security.jwt.JwtProvider;
import com.dokanne.DokaneeBackend.jwt.services.SignUpAndSignInService;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.product.v1.ProfileModel;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProfileService {

    private final SignUpAndSignInService signUpAndSignInService;
    private final ProfileRepository profileRepository;
    private final JwtProvider jwtProvider;

    public ResponseEntity<Object> getUserProfile() {
        ResponseEntity<UserResponse> userResEntity = signUpAndSignInService.getLoggedAuthUser();

        if (userResEntity.getStatusCodeValue() == 200) {
            System.out.println(1);
            System.out.println(userResEntity.getBody().getUsername());
            ProfileModel profileModel = profileRepository.findByUserName(userResEntity.getBody().getUsername());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "OK");
            return new ResponseEntity<Object>(ProfileResponse.builder()
                    .firstName(profileModel.getFirstName())
                    .lastName(profileModel.getLastName())
                    .email(profileModel.getEmail())
                    .phone(profileModel.getPhone())
                    .dob(profileModel.getDob())
                    .nid(profileModel.getNid())
                    .address(profileModel.getAddress())
                    .userName(profileModel.getUserName())
                    .photoLink(profileModel.getPhotoLink())
                    .storeIds(profileModel.getStoreIds())
                    .build(),
                    httpHeaders,
                    HttpStatus.OK
            );

        } else if (userResEntity.getStatusCodeValue() == 401) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "UnAuthorized");
            return new ResponseEntity<>("UnAuthorized", httpHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(userResEntity.getBody(), userResEntity.getHeaders(), userResEntity.getStatusCode());
        }

    }

    public List<String> getStoreIdStringFromStoreId(List<StoreIds> storeIdsList) {
        List<String> list = new ArrayList<>();
        for (StoreIds storeIds : storeIdsList) {
            list.add(storeIds.getStoreId());
        }
        return list;
    }

    public ResponseEntity<Object> editUserInfo(ProfileRequest profileRequest) {
        ResponseEntity<UserResponse> userResEntity = signUpAndSignInService.getLoggedAuthUser();

        if (userResEntity.getStatusCodeValue() == 200) {
            ProfileModel profileModel = profileRepository.findByUserName(Objects.requireNonNull(userResEntity.getBody()).getUsername());

            profileModel.setFirstName(profileRequest.getFirstName());
            profileModel.setLastName(profileRequest.getLastName());
            profileModel.setDob(profileRequest.getDob());
            profileModel.setAddress(profileRequest.getAddress());

            profileRepository.save(profileModel);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Massage", "Everything is ok");
            return new ResponseEntity<>("OK", httpHeaders, HttpStatus.OK);

        } else if (userResEntity.getStatusCodeValue() == 401) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "UnAuthorized");
            return new ResponseEntity<>("Unauthorized", httpHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<Object>(userResEntity.getBody(), userResEntity.getHeaders(), userResEntity.getStatusCode());
        }

    }

    public ResponseEntity<ApiResponse<String>> uploadImage(MultipartFile aFile, String token) throws Exception {
        String userName = jwtProvider.getUserNameFromJwt(token);

        ProfileModel profileModel = profileRepository.findByUserName(userName);
        MultipartFile[] mp = new MultipartFile[1];
        mp[0] = aFile;

        List<String> urls = UserUtils.uploadImage(mp);
        profileModel.setPhotoLink(urls.get(0));
        profileRepository.save(profileModel);

        return new ResponseEntity<>(new ApiResponse<>(201, "Image Upload Successful", urls.get(0)), HttpStatus.OK);

    }
}

