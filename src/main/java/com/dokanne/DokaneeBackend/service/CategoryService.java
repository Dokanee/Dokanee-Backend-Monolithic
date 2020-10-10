package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.dto.response.AllCategoryResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.SubCategoryModel;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.SubCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final StoreService storeService;


    public ResponseEntity addCategory(CategoryRequest categoryRequest) {
        List<String> storeList = getStoreStringIds(storeService.getAuthUserInfo().getStoreIds());
        boolean storeIdAuth = storeList.contains(categoryRequest.getStoreId());

        if (storeIdAuth) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setCategoryId(UUID.randomUUID().toString());
            categoryModel.setStoreId(categoryRequest.getStoreId());
            categoryModel.setCategoryName(categoryRequest.getCategoryName());
            categoryModel.setSlug(categoryRequest.getSlug());

            categoryRepository.save(categoryModel);

            return new ResponseEntity("Created " + categoryRequest.getCategoryName(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Not Permitted To Create Category", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity addSubCategory(SubCategoryRequest subCategoryRequest) {
        List<String> storeList = getStoreStringIds(storeService.getAuthUserInfo().getStoreIds());
        boolean storeIdAuth = storeList.contains(subCategoryRequest.getStoreId());

        if (storeIdAuth) {
            SubCategoryModel subCategoryModel = new SubCategoryModel(UUID.randomUUID().toString(),
                    subCategoryRequest.getSubCategoryName(), subCategoryRequest.getSlug());

            CategoryModel categoryModel = categoryRepository.findById(subCategoryRequest.getCategoryId()).get();

            List<SubCategoryModel> subCategoryModelList = categoryModel.getSubCategoryModels();
            subCategoryModelList.add(subCategoryModel);

            categoryRepository.save(categoryModel);

            subCategoryRepository.save(subCategoryModel);

            return new ResponseEntity("Created " + subCategoryRequest.getSubCategoryName(), HttpStatus.CREATED);

        } else {
            return new ResponseEntity("Store Not Authenticated ", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity getCategory(String storeId) {
        List<String> storeList = getStoreStringIds(storeService.getAuthUserInfo().getStoreIds());
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            List<CategoryModel> categoryModelList = categoryRepository.findAllByStoreId(storeId);

            return new ResponseEntity(categoryModelList, HttpStatus.OK);
        } else {
            return new ResponseEntity("Store not Authenticated", HttpStatus.UNAUTHORIZED);
        }


    }

    List<String> getStoreStringIds(List<StoreIds> storeIdsList) {
        List<String> storeIdsString = new ArrayList();
        for (StoreIds storeIds : storeIdsList) {
            storeIdsString.add(storeIds.getStoreId());
        }

        return storeIdsString;
    }

    public ResponseEntity getAllCategory(String storeId) {
        List<String> storeList = getStoreStringIds(storeService.getAuthUserInfo().getStoreIds());
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            List<CategoryModel> categoryModels = categoryRepository.findAllByStoreId(storeId);

            List<AllCategoryResponse> categoryList = new ArrayList();

            for (CategoryModel categoryModel : categoryModels) {
                AllCategoryResponse allCategoryResponse = new AllCategoryResponse(categoryModel.getCategoryId(), categoryModel.getCategoryName());
                categoryList.add(allCategoryResponse);
            }


            return new ResponseEntity(categoryList, HttpStatus.OK);
        } else {
            return new ResponseEntity("Store Not Authenticated", HttpStatus.UNAUTHORIZED);
        }

    }

}
