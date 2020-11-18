package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard/store/{storeId}")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity addCategory(@PathVariable String storeId, @RequestBody CategoryRequest categoryRequest) {
        return categoryService.addCategory(categoryRequest, storeId);
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getCategory(@PathVariable String storeId) {
        return categoryService.getCategory(storeId);
    }

    @GetMapping("/category/all")
    public ResponseEntity getAllCategory(@PathVariable String storeId) {
        return categoryService.getAllCategory(storeId);
    }

    @PostMapping("/subCategory")
    public ResponseEntity addSubCategory(@PathVariable String storeId, @RequestBody SubCategoryRequest subCategoryRequest) {
        return categoryService.addSubCategory(subCategoryRequest, storeId);
    }


}
