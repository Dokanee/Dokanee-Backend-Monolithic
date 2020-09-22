package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.model.CategoryModel;
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

    public ResponseEntity addCategory(CategoryRequest categoryRequest) {
        CategoryModel categoryModel = new CategoryModel(UUID.randomUUID().toString(), categoryRequest.getStoreId(), categoryRequest.getCategoryName(), categoryRequest.getSlug());
        categoryRepository.save(categoryModel);

        return new ResponseEntity("Created " + categoryRequest.getCategoryName(), HttpStatus.CREATED);
    }

    public ResponseEntity addSubCategory(SubCategoryRequest subCategoryRequest) {
        SubCategoryModel subCategoryModel = new SubCategoryModel(UUID.randomUUID().toString(), subCategoryRequest.getCategoryId(), subCategoryRequest.getStoreId(), subCategoryRequest.getSubCategoryName(), subCategoryRequest.getSlug());
        subCategoryRepository.save(subCategoryModel);

        return new ResponseEntity("Created " + subCategoryRequest.getSubCategoryName(), HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getCategory() {

        List<CategoryModel> categoryModelList = categoryRepository.findAll();

        List<CategoryModel> categoryModels = new ArrayList<>();

        categoryModels.addAll(categoryModelList);

        return new ResponseEntity<Object>(categoryModels, HttpStatus.OK);
    }
}
