package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.dto.response.MassageResponse;
import com.dokanne.DokaneeBackend.dto.response.StoreInfoResponse;
import com.dokanne.DokaneeBackend.jwt.dto.response.OwnerProfileResponse;
import com.dokanne.DokaneeBackend.jwt.model.Role;
import com.dokanne.DokaneeBackend.model.ProfileModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class StoreService {


    private final ProfileRepository profileRepository;
    private final StoreRepository storeRepository;
    private final ProfileRepository opRepo;

    public OwnerProfileResponse getAuthUserInfo() {

        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OwnerProfileResponse response;

        if (authUser instanceof UserDetails) {

            String username = ((UserDetails) authUser).getUsername();
            ProfileModel data = opRepo.findByUserName(username);

            response = new OwnerProfileResponse("OK", data.getOwnerId(), data.getFirstName(), data.getLastName(), data.getEmail(), data.getPhone(), data.getAddress(), data.getStoreIds());

            return response;

        } else if (authUser instanceof UserDetails == false) {
            response = new OwnerProfileResponse();
            response.setMassage("Bearer Token Problem");
            return response;

        } else {
            String username = authUser.toString();

            System.out.println(username);
        }
        response = new OwnerProfileResponse();
        response.setMassage("Not Found");
        return response;

    }

    private Set<String> getRolesToString(Set<Role> roles2) {
        Set<String> roles = new HashSet<>();
        for (Role role : roles2) {

            roles.add(role.getName().toString());
        }
        return roles;
    }

    public ResponseEntity<String> createStore(StoreRequest storeRequest) {

        String id = UUID.randomUUID().toString();
        StoreModel storeModel = new StoreModel(id, getAuthUserInfo().getOwnerId(), storeRequest.getStoreName(),
                storeRequest.getStoreInfo(), (getAuthUserInfo().getFirstName() + " " + getAuthUserInfo().getLastName()), storeRequest.getSubDomainName(), storeRequest.getStoreCategory(), storeRequest.getAddress(),
                storeRequest.getUpzila(), storeRequest.getZila(), storeRequest.getDivision());
        storeModel.setHavePhysicalStore(storeRequest.isHavePhysicalStore());

        storeRepository.save(storeModel);

        ProfileModel profileModel = profileRepository.findById(getAuthUserInfo().getOwnerId()).get();

        List<StoreIds> storeIdsList = profileModel.getStoreIds();
        StoreIds storeIds = new StoreIds();
        storeIds.setStoreId(id);
        storeIdsList.add(storeIds);
        profileModel.setStoreIds(storeIdsList);

        profileRepository.save(profileModel);

        return new ResponseEntity<String>("Store Created", HttpStatus.CREATED);
    }


    public ResponseEntity<Object> getStoreInfo() {
        List<StoreModel> storeModelListOptional = storeRepository.findAllByOwnerId(getAuthUserInfo().getOwnerId());
        if (storeModelListOptional.isEmpty()) {
            return new ResponseEntity<>("No Data", HttpStatus.BAD_REQUEST);
        }

        List<StoreInfoResponse> storeInfoResponseList = new ArrayList<>();

        for (StoreModel storeModel : storeModelListOptional) {
            StoreInfoResponse storeInfoResponse = new StoreInfoResponse(storeModel.getStoreId(), storeModel.getStoreName(), storeModel.getStoreInfo(),
                    storeModel.getOwnerName(), storeModel.getDomainName(), storeModel.getSubDomainName(),
                    storeModel.getStoreCategory(), storeModel.isHavePhysicalStore(), storeModel.getAddress(),
                    storeModel.getUpzila(), storeModel.getZila(), storeModel.getDivision());

            storeInfoResponseList.add(storeInfoResponse);

        }

        return new ResponseEntity<>(storeInfoResponseList, HttpStatus.OK);

    }

    public ResponseEntity<Boolean> checkSubDomain(String subDomain) {
        Optional<StoreModel> storeModelOptional = storeRepository.findBySubDomainName(subDomain);

        if (storeModelOptional.isPresent()) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }

    public ResponseEntity<Boolean> checkDomain(String domain) {
        Optional<StoreModel> storeModelOptional = storeRepository.findByDomainName(domain);

        if (storeModelOptional.isPresent()) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }

    public ResponseEntity<MassageResponse> editStore(String storeId, StoreRequest storeRequest) {
        Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

        if (storeModelOptional.isPresent()) {
            StoreModel storeModel = storeModelOptional.get();

            storeModel.setStoreName(storeRequest.getStoreName());
            storeModel.setStoreInfo(storeRequest.getStoreInfo());
            storeModel.setStoreCategory(storeRequest.getStoreCategory());
            storeModel.setAddress(storeRequest.getAddress());
            storeModel.setUpzila(storeRequest.getUpzila());
            storeModel.setZila(storeRequest.getZila());
            storeModel.setDivision(storeRequest.getDivision());
            storeModel.setHavePhysicalStore(storeRequest.isHavePhysicalStore());

            storeRepository.save(storeModel);

            return new ResponseEntity<>(new MassageResponse("Info Saved Successfully", storeModel, 200), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new MassageResponse("No store found on that ID", 406), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}

