package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.EditSubCategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.SubCategoryModel;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.SubCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor

@Service
public class SubCategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final StoreService storeService;

    public ResponseEntity<String> addSubCategory(SubCategoryRequest subCategoryRequest, String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            String subCategoryId = UUID.randomUUID().toString();
            String subCategorySlug = subCategoryRequest.getSubCategoryName().toLowerCase();
            subCategorySlug = subCategorySlug.replace(" ", "-");
            subCategorySlug = subCategorySlug + "-" + storeId.substring(0, 3) + "-" + subCategoryId.substring(0, 3);

            SubCategoryModel subCategoryModel = new SubCategoryModel(subCategoryId,
                    subCategoryRequest.getSubCategoryName(), subCategoryRequest.getSubCategoryIcon(), subCategorySlug);

            Optional<CategoryModel> categoryModelOptional = categoryRepository.findById(subCategoryRequest.getCategoryId());

            if (categoryModelOptional.isPresent()) {
                CategoryModel categoryModel = categoryModelOptional.get();

                List<SubCategoryModel> subCategoryModelList = categoryModel.getSubCategoryModels();
                subCategoryModelList.add(subCategoryModel);

                categoryRepository.save(categoryModel);

//                subCategoryRepository.save(subCategoryModel);

                return new ResponseEntity<>("Created " + subCategoryRequest.getSubCategoryName(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Category id isn't found", HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>("Store Not Authenticated ", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<ApiResponse<IdResponse>> edtSubCategory(EditSubCategoryRequest subCategoryRequest, String storeId, String subCategoryId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<SubCategoryModel> subCategoryModelOptional = subCategoryRepository.findById(subCategoryId);

            if (subCategoryModelOptional.isPresent()) {
                SubCategoryModel subCategoryModel = subCategoryModelOptional.get();
                Optional<CategoryModel> categoryModelOptional =
                        categoryRepository.findBySubCategoryModelsAndStoreId(subCategoryModel, storeId);

                if (categoryModelOptional.isPresent()) {
                    subCategoryModel.setSubCategoryIcon(subCategoryRequest.getSubCategoryIcon());
                    subCategoryModel.setSubCategoryName(subCategoryRequest.getSubCategoryName());

                    subCategoryRepository.save(subCategoryModel);

                    return new ResponseEntity<>(new ApiResponse<>(200, "SubCategory Edit Successful",
                            new IdResponse(subCategoryModel.getSubCategoryId())), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Edit This SubCategory");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SubCategory Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<IdResponse>> edtSubCategory(String storeId, String subCategoryId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<SubCategoryModel> subCategoryModelOptional = subCategoryRepository.findById(subCategoryId);

            if (subCategoryModelOptional.isPresent()) {
                SubCategoryModel subCategoryModel = subCategoryModelOptional.get();
                Optional<CategoryModel> categoryModelOptional =
                        categoryRepository.findBySubCategoryModelsAndStoreId(subCategoryModel, storeId);

                if (categoryModelOptional.isPresent()) {
                    CategoryModel categoryModel = categoryModelOptional.get();

                    List<SubCategoryModel> subCategoryModels = categoryModel.getSubCategoryModels();
                    subCategoryModels.remove(subCategoryModel);
                    categoryModel.setSubCategoryModels(subCategoryModels);

                    categoryRepository.save(categoryModel);
                    subCategoryRepository.deleteById(subCategoryId);

                    return new ResponseEntity<>(new ApiResponse<>(200, "SubCategory Delete Successful",
                            new IdResponse(subCategoryModel.getSubCategoryId())), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Delete This SubCategory");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SubCategory Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }
}
