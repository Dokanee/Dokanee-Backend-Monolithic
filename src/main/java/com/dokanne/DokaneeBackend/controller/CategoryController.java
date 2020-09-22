package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin("*")

@RestController
@RequestMapping("/store")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category/add")
    public ResponseEntity addCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.addCategory(categoryRequest);
    }

    @GetMapping("/category/")
    public ResponseEntity<Object> getCategory() {
        return categoryService.getCategory();
    }

    @PostMapping("/subCategory/add")
    public ResponseEntity addSubCategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        return categoryService.addSubCategory(subCategoryRequest);
    }


}
