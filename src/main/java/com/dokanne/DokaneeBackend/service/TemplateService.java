package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.TemplateRequest;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.model.TemplateModel;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import com.dokanne.DokaneeBackend.repository.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final StoreService storeService;
    private final StoreRepository storeRepository;


    public String createTemplate(String storeId) {
        StoreModel storeModel = storeRepository.findById(storeId).get();

        Optional<TemplateModel> template = templateRepository.findByStoreId(storeId);
        if (template.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Template of this store is already available");
        }

        String id = UUID.randomUUID().toString();
        TemplateModel templateModel = TemplateModel.builder()
                .id(id)
                .storeId(storeId)
                .subDomain(storeModel.getSubDomainName())
                .build();

        templateRepository.save(templateModel);

        return id;
    }

    public ResponseEntity<ApiResponse<TemplateModel>> getTemplateInfo(String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<TemplateModel> templateModelOptional = templateRepository.findByStoreId(storeId);

            if (templateModelOptional.isPresent()) {
                TemplateModel templateModel = templateModelOptional.get();

                return new ResponseEntity<>(new ApiResponse<>(200, "Template Found", templateModel), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template Not Found.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<IdResponse>> editTemplate(String storeId, TemplateRequest templateRequest) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<TemplateModel> templateModelOptional = templateRepository.findByStoreId(storeId);

            if (templateModelOptional.isPresent()) {
                TemplateModel templateModel = templateModelOptional.get();
                templateModel.setPrimaryColor(templateRequest.getPrimaryColor());
                templateModel.setTemplateId(templateRequest.getTemplateId());
                templateModel.setSecondaryColor(templateRequest.getSecondaryColor());

                templateRepository.save(templateModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Template Edit Successful", new IdResponse(templateModel.getId())), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template Not Found.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<List<String>>> uploadSliderImage(String storeId, MultipartFile[] aFile) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<TemplateModel> templateModelOptional = templateRepository.findByStoreId(storeId);

            if (templateModelOptional.isPresent()) {
                TemplateModel templateModel = templateModelOptional.get();
                if ((templateModel.getSliderImages().size() + aFile.length) > 5) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Can't set more than 5 slider image. Try Deleting previous items or reduce upload image quantity");
                }
                List<String> sliderImageModel = templateModel.getSliderImages();
                List<String> imageLinks;

                try {
                    imageLinks = UserUtils.uploadImage(aFile);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
                }

                sliderImageModel.addAll(imageLinks);

                templateRepository.save(templateModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Image Upload Successful", sliderImageModel), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template Not Found.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }

    }

    public ResponseEntity<ApiResponse<List<String>>> deleteSliderImage(String storeId, List<String> imageUrls) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<TemplateModel> templateModelOptional = templateRepository.findByStoreId(storeId);

            if (templateModelOptional.isPresent()) {
                TemplateModel templateModel = templateModelOptional.get();
                List<String> sliderImageModel = templateModel.getSliderImages();

                sliderImageModel.removeAll(imageUrls);

                templateRepository.save(templateModel);

                return new ResponseEntity<>(new ApiResponse<>(200, "Image Delete Successful", sliderImageModel), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Template Not Found.");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }
}
