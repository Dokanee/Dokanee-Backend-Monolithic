package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.request.ProfileRequest;
import com.dokanne.DokaneeBackend.dto.response.MassageResponse;
import com.dokanne.DokaneeBackend.dto.response.ProfileResponse;
import com.dokanne.DokaneeBackend.jwt.dto.response.UserResponse;
import com.dokanne.DokaneeBackend.jwt.services.SignUpAndSignInService;
import com.dokanne.DokaneeBackend.model.ProfileModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProfileService {

    private final SignUpAndSignInService signUpAndSignInService;
    private final ProfileRepository profileRepository;

    public ResponseEntity getUserProfile() {
        ResponseEntity<UserResponse> userResEntity = signUpAndSignInService.getLoggedAuthUser();

        if (userResEntity.getStatusCodeValue() == 200) {
            System.out.println(1);
            System.out.println(userResEntity.getBody().getUsername());
            ProfileModel profileModel = profileRepository.findByUserName(userResEntity.getBody().getUsername());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "OK");
            return new ResponseEntity(new MassageResponse("Ok", ProfileResponse.builder()
                    .firstName(profileModel.getFirstName())
                    .lastName(profileModel.getLastName())
                    .email(profileModel.getEmail())
                    .phone(profileModel.getPhone())
                    .dob(profileModel.getDob())
                    .nid(profileModel.getNid())
                    .address(profileModel.getAddress())
                    .userName(profileModel.getUserName())
                    .photoLink(profileModel.getPhotoLink())
                    .storeIds(getStoreIdStringFromStoreId(profileModel.getStoreIds()))
                    .build(),
                    200),
                    httpHeaders,
                    HttpStatus.OK
            );

        } else if (userResEntity.getStatusCodeValue() == 401) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "UnAuthorized");
            return new ResponseEntity(new MassageResponse("UnAuthorized", 401), httpHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity(new MassageResponse("UnAuthorized", userResEntity.getStatusCodeValue()), userResEntity.getHeaders(), userResEntity.getStatusCode());
        }

    }

    public List<String> getStoreIdStringFromStoreId(List<StoreIds> storeIdsList) {
        List<String> list = new ArrayList();
        for (StoreIds storeIds : storeIdsList) {
            list.add(storeIds.getStoreId());
        }
        return list;
    }

    public ResponseEntity editUserInfo(ProfileRequest profileRequest) {
        ResponseEntity<UserResponse> userResEntity = signUpAndSignInService.getLoggedAuthUser();

        if (userResEntity.getStatusCodeValue() == 200) {
            ProfileModel profileModel = profileRepository.findByUserName(userResEntity.getBody().getUsername());

            profileModel.setFirstName(profileRequest.getFirstName());
            profileModel.setLastName(profileRequest.getLastName());
            profileModel.setDob(profileRequest.getDob());
            profileModel.setAddress(profileRequest.getAddress());

            profileRepository.save(profileModel);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Massage", "Everything is ok");
            return new ResponseEntity(new MassageResponse("OK", 200), httpHeaders, HttpStatus.OK);

        } else if (userResEntity.getStatusCodeValue() == 401) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "UnAuthorized");
            return new ResponseEntity(new ProfileResponse(), httpHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity(new ProfileResponse(), userResEntity.getHeaders(), userResEntity.getStatusCode());
        }

    }
}
