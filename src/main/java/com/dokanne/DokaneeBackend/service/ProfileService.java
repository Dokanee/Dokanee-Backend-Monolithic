package com.dokanne.DokaneeBackend.service;

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

            if (profileModel != null) {
                System.out.println(profileModel.getEmail());
                System.out.println(2);
            }
            System.out.println(3);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "OK");
            return new ResponseEntity(ProfileResponse.builder()
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
                    httpHeaders,
                    HttpStatus.OK
            );

        } else if (userResEntity.getStatusCodeValue() == 401) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("massage", "UnAuthorized");
            return new ResponseEntity(new ProfileResponse(), httpHeaders, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity(new ProfileResponse(), userResEntity.getHeaders(), userResEntity.getStatusCode());
        }

    }

    public List<String> getStoreIdStringFromStoreId(List<StoreIds> storeIdsList) {
        List<String> list = new ArrayList();
        for (StoreIds storeIds : storeIdsList) {
            list.add(storeIds.getStoreId());
        }
        return list;
    }

}
