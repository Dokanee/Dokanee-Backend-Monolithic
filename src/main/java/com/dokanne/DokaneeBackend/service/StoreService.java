package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.dto.response.StoreInfoResponse;
import com.dokanne.DokaneeBackend.jwt.dto.response.OwnerProfileResponse;
import com.dokanne.DokaneeBackend.jwt.model.Role;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.model.product.v1.ProfileModel;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    public ResponseEntity<ApiResponse<IdResponse>> createStore(StoreRequest storeRequest) {

        String id = UUID.randomUUID().toString();
        StoreModel storeModel = new StoreModel(id, getAuthUserInfo().getOwnerId(), storeRequest.getStoreName(),
                storeRequest.getStoreInfo(), (getAuthUserInfo().getFirstName() + " " + getAuthUserInfo().getLastName()),
                storeRequest.getFacebookLink(), storeRequest.getYoutubeLink(), storeRequest.getGoogleMapLink(),
                storeRequest.getSubDomainName(), storeRequest.getStoreCategory(), storeRequest.getAddress(),
                storeRequest.getUpzila(), storeRequest.getZila(), storeRequest.getDivision());
        storeModel.setHavePhysicalStore(storeRequest.isHavePhysicalStore());

        storeRepository.save(storeModel);

        ProfileModel profileModel = profileRepository.findById(getAuthUserInfo().getOwnerId()).get();

        List<String> storeIdsList = profileModel.getStoreIds();
//        StoreIds storeIds = new StoreIds();
//        storeIds.setStoreId(id);
        storeIdsList.add(id);
        profileModel.setStoreIds(storeIdsList);

        profileRepository.save(profileModel);

        return new ResponseEntity<>(new ApiResponse<>(201, "Store Create Successful",
                new IdResponse(storeModel.getStoreId())), HttpStatus.CREATED);
    }


    public ResponseEntity<ApiResponse<List<StoreInfoResponse>>> getStoreInfo() {
        List<StoreModel> storeModelListOptional = storeRepository.findAllByOwnerId(getAuthUserInfo().getOwnerId());
        List<StoreInfoResponse> storeInfoResponseList = new ArrayList<>();

        if (storeModelListOptional.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(204, "No Store Found", storeInfoResponseList), HttpStatus.NO_CONTENT);
        }

        for (StoreModel storeModel : storeModelListOptional) {
            StoreInfoResponse storeInfoResponse = new StoreInfoResponse(storeModel.getStoreId(), storeModel.getStoreName(),
                    storeModel.getStoreInfo(), storeModel.getStoreLogo(), storeModel.getFacebookLink(), storeModel.getYoutubeLink(),
                    storeModel.getGoogleMapLink(), storeModel.getOwnerName(), storeModel.getDomainName(), storeModel.getSubDomainName(),
                    storeModel.getStoreCategory(), storeModel.isHavePhysicalStore(), storeModel.getAddress(),
                    storeModel.getUpzila(), storeModel.getZila(), storeModel.getDivision());

            storeInfoResponseList.add(storeInfoResponse);

        }
        return new ResponseEntity<>(new ApiResponse<>(200, "Ok.\nStore Found", storeInfoResponseList), HttpStatus.OK);

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

    public ResponseEntity<ApiResponse<IdResponse>> editStore(String storeId, StoreRequest storeRequest) {
        List<String> storeList = getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                StoreModel storeModel = storeModelOptional.get();

                storeModel.setStoreName(storeRequest.getStoreName());
                storeModel.setFacebookLink(storeRequest.getFacebookLink());
                storeModel.setYoutubeLink(storeRequest.getYoutubeLink());
                storeModel.setGoogleMapLink(storeRequest.getGoogleMapLink());
                storeModel.setStoreInfo(storeRequest.getStoreInfo());
                storeModel.setStoreCategory(storeRequest.getStoreCategory());
                storeModel.setAddress(storeRequest.getAddress());
                storeModel.setUpzila(storeRequest.getUpzila());
                storeModel.setZila(storeRequest.getZila());
                storeModel.setDivision(storeRequest.getDivision());
                storeModel.setHavePhysicalStore(storeRequest.isHavePhysicalStore());

                storeRepository.save(storeModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Store Edit Successful",
                        new IdResponse(storeModel.getStoreId())), HttpStatus.OK);

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Store Found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }

    }

    public ResponseEntity<StoreModel> getStoreInfoBySubDomain(String subDomain) {
        StoreModel storeModel = storeRepository.findBySubDomainName(subDomain).get();

        return new ResponseEntity<>(storeModel, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<IdResponse>> deleteStore(String storeId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                storeRepository.deleteById(storeId);

                ProfileModel profileModel = profileRepository.findById(getAuthUserInfo().getOwnerId()).get();

                List<String> storeIdsList = profileModel.getStoreIds();

                storeIdsList.remove(storeId);
                profileModel.setStoreIds(storeIdsList);

                profileRepository.save(profileModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Store Delete Successful",
                        new IdResponse(storeId)), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Store Found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<String>> uploadStoreLogo(MultipartFile aFile, String storeId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                StoreModel storeModel = storeModelOptional.get();
                MultipartFile[] multipartFiles = new MultipartFile[1];
                multipartFiles[0] = aFile;
                List<String> logoLinks = new ArrayList<>();

                try {
                    logoLinks = UserUtils.uploadImage(multipartFiles);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
                }

                storeModel.setStoreLogo(logoLinks.get(0));
                storeRepository.save(storeModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Store Logo Upload SuccessFull Successful",
                        logoLinks.get(0)), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Store Found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<String>> deleteStoreLogo(String storeId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                StoreModel storeModel = storeModelOptional.get();
                storeModel.setStoreLogo(null);

                storeRepository.save(storeModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Store Logo Delete SuccessFull Successful",
                        null), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Store Found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }
}

