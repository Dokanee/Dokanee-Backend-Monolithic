package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.response.AllCategoryResponse;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.StoreModel;
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
                String categoryId = UUID.randomUUID().toString();
                String categorySlug = categoryRequest.getCategoryName().toLowerCase();
                categorySlug = categorySlug.replace(" ", "-");
                categorySlug = categorySlug + "-" + storeId.substring(0, 3) + "-" + categoryId.substring(0, 3);

                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setCategoryId(categoryId);
                categoryModel.setCategoryIcon(categoryRequest.getCategoryIcon());
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

    public ResponseEntity<List<CategoryModel>> getCategory(String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            List<CategoryModel> categoryModelList = categoryRepository.findAllByStoreId(storeId);
            return new ResponseEntity<>(categoryModelList, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Store is Not Authenticated");
        }


    }

    List<String> getStoreStringIds(List<StoreIds> storeIdsList) {
        List<String> storeIdsString = new ArrayList<>();
        for (StoreIds storeIds : storeIdsList) {
            storeIdsString.add(storeIds.getStoreId());
        }

        return storeIdsString;
    }

    public ResponseEntity<List<AllCategoryResponse>> getAllCategory(String storeId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            List<CategoryModel> categoryModels = categoryRepository.findAllByStoreId(storeId);

            List<AllCategoryResponse> categoryList = new ArrayList<>();

            for (CategoryModel categoryModel : categoryModels) {
                AllCategoryResponse allCategoryResponse = new AllCategoryResponse(categoryModel.getCategoryId(), categoryModel.getCategoryIcon(), categoryModel.getCategoryName());
                categoryList.add(allCategoryResponse);
            }

            return new ResponseEntity<>(categoryList, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Store is Not Authenticated");
        }

    }

    public ResponseEntity<ApiResponse<IdResponse>> editCategory(CategoryRequest categoryRequest, String storeId, String categoryId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<CategoryModel> categoryModelOptional = categoryRepository.findById(categoryId);

            if (categoryModelOptional.isPresent()) {
                CategoryModel categoryModel = categoryModelOptional.get();

                if (categoryModel.getStoreId().equals(storeId)) {
                    categoryModel.setCategoryName(categoryRequest.getCategoryName());
                    categoryModel.setCategoryIcon(categoryRequest.getCategoryIcon());

                    categoryRepository.save(categoryModel);

                    return new ResponseEntity<>(new ApiResponse<>(200, "Category Edit Successful",
                            new IdResponse(categoryModel.getCategoryId())), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Edit This Category");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }

    public ResponseEntity<ApiResponse<IdResponse>> deleteCategory(String storeId, String categoryId) {
        List<String> storeList = storeService.getAuthUserInfo().getStoreIds();
        boolean storeIdAuth = storeList.contains(storeId);

        if (storeIdAuth) {
            Optional<CategoryModel> categoryModelOptional = categoryRepository.findById(categoryId);

            if (categoryModelOptional.isPresent()) {
                CategoryModel categoryModel = categoryModelOptional.get();

                if (categoryModel.getStoreId().equals(storeId)) {
                    categoryRepository.deleteById(categoryId);

                    return new ResponseEntity<>(new ApiResponse<>(200, "Category Deleted Successful",
                            new IdResponse(categoryModel.getCategoryId())), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Delete This Category");
                }

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You Are Not Permitted To Do This Operation. Your are not Authorized on this store.");
        }
    }
}
