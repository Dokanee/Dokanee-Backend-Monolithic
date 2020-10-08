package com.dokanne.DokaneeBackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dokanne.DokaneeBackend.dto.request.ProfileRequest;
import com.dokanne.DokaneeBackend.dto.response.MassageResponse;
import com.dokanne.DokaneeBackend.dto.response.ProfileResponse;
import com.dokanne.DokaneeBackend.jwt.dto.response.UserResponse;
import com.dokanne.DokaneeBackend.jwt.services.SignUpAndSignInService;
import com.dokanne.DokaneeBackend.model.ProfileModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public ResponseEntity uploadImage(MultipartFile aFile) {
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "to-let-app",
                "api_key", "111257839862595",
                "api_secret", "7H1QY2G1W6FVQQ3envantRuJz4c"));

        try {
            ProfileModel profileModel = profileRepository.findByUserName(signUpAndSignInService.getLoggedAuthUser().getBody().getUsername());

            System.out.println(1);
            File f = Files.createTempFile("temp", aFile.getOriginalFilename()).toFile();
            System.out.println(2);
            aFile.transferTo(f);
            System.out.println(3);
            Map response = c.uploader().upload(f, ObjectUtils.emptyMap());
            System.out.println(4);
            JSONObject json = new JSONObject(response);
            System.out.println(5);
            String url = json.getString("url");
            System.out.println(6);
            profileModel.setPhotoLink(url);

            profileRepository.save(profileModel);

            return new ResponseEntity("{\"status\":\"OK\"}", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<String>("Wrong", HttpStatus.BAD_REQUEST);
        }
    }
}
