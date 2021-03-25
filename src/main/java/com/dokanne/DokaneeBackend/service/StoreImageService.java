package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StoreImageService {
    private final StoreService storeService;
    private final StoreRepository storeRepository;


    public ResponseEntity<ApiResponse<String>> uploadStoreLogo(MultipartFile aFile, String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
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
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
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

    public ResponseEntity<ApiResponse<List<String>>> uploadStoreImages(String storeId, MultipartFile[] aFile) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                StoreModel storeModel = storeModelOptional.get();

                if ((storeModel.getStoreImages().size() + aFile.length) > 4) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Can't set more than 4 Store image. Try Deleting previous items or reduce upload image quantity");
                }
                List<String> storeImageModel = storeModel.getStoreImages();
                List<String> imageLinks;

                try {
                    imageLinks = UserUtils.uploadImage(aFile);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
                }

                storeImageModel.addAll(imageLinks);
                storeModel.setStoreImages(storeImageModel);

                storeRepository.save(storeModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Image Upload Successful", storeImageModel), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template Not Found.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<List<String>>> deleteStoreImage(String storeId, List<String> imageUrls) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                StoreModel storeModel = storeModelOptional.get();

                List<String> storeImageModel = storeModel.getStoreImages();

                storeImageModel.removeAll(imageUrls);
                storeModel.setStoreImages(storeImageModel);

                storeRepository.save(storeModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Image Delete Successful", storeImageModel), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template Not Found.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }
}
