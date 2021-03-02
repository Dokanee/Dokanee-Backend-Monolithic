package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.dto.response.AllCategoryResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.model.SubCategoryModel;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import com.dokanne.DokaneeBackend.repository.SubCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final StoreService storeService;
    private final StoreRepository storeRepository;


    public ResponseEntity<String> addCategory(CategoryRequest categoryRequest, String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<StoreModel> storeModelOptional = storeRepository.findById(storeId);

            if (storeModelOptional.isPresent()) {
                String categorySlug = categoryRequest.getCategoryName().toLowerCase();
                categorySlug = categorySlug.replace(" ", "-");
                categorySlug = categorySlug + "-" + storeId.substring(0, 7);

                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setCategoryId(UUID.randomUUID().toString());
                categoryModel.setStoreId(storeId);
                categoryModel.setSubDomain(storeModelOptional.get().getSubDomainName());
                categoryModel.setCategoryName(categoryRequest.getCategoryName());
                categoryModel.setSlug(categorySlug);

                categoryRepository.save(categoryModel);

                return new ResponseEntity<>("Created " + categoryRequest.getCategoryName(), HttpStatus.CREATED);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Store Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Permitted To Create Category");
        }

    }

    public ResponseEntity<String> addSubCategory(SubCategoryRequest subCategoryRequest, String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            String subCategorySlug = subCategoryRequest.getSubCategoryName().toLowerCase();
            subCategorySlug = subCategorySlug.replace(" ", "-");
            subCategorySlug = subCategorySlug + "-" + storeId.substring(0, 7);

            SubCategoryModel subCategoryModel = new SubCategoryModel(UUID.randomUUID().toString(),
                    subCategoryRequest.getSubCategoryName(), subCategorySlug);

            Optional<CategoryModel> categoryModelOptional = categoryRepository.findById(subCategoryRequest.getCategoryId());

            if (categoryModelOptional.isPresent()) {
                CategoryModel categoryModel = categoryModelOptional.get();

                List<SubCategoryModel> subCategoryModelList = categoryModel.getSubCategoryModels();
                subCategoryModelList.add(subCategoryModel);

                categoryRepository.save(categoryModel);

                subCategoryRepository.save(subCategoryModel);

                return new ResponseEntity<>("Created " + subCategoryRequest.getSubCategoryName(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Category id isn't found", HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>("Store Not Authenticated ", HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<Object> getCategory(String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            List<CategoryModel> categoryModelList = categoryRepository.findAllByStoreId(storeId);
            return new ResponseEntity<Object>(categoryModelList, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Store is not Authenticated", HttpStatus.UNAUTHORIZED);
        }


    }

    List<String> getStoreStringIds(List<StoreIds> storeIdsList) {
        List<String> storeIdsString = new ArrayList<>();
        for (StoreIds storeIds : storeIdsList) {
            storeIdsString.add(storeIds.getStoreId());
        }

        return storeIdsString;
    }

    public ResponseEntity<Object> getAllCategory(String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            List<CategoryModel> categoryModels = categoryRepository.findAllByStoreId(storeId);

            List<AllCategoryResponse> categoryList = new ArrayList<>();

            for (CategoryModel categoryModel : categoryModels) {
                AllCategoryResponse allCategoryResponse = new AllCategoryResponse(categoryModel.getCategoryId(), categoryModel.getCategoryName());
                categoryList.add(allCategoryResponse);
            }

            return new ResponseEntity<Object>(categoryList, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>("Store is Not Authenticated", HttpStatus.UNAUTHORIZED);
        }

    }

}
