package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.CategoryRequest;
import com.dokanne.DokaneeBackend.dto.response.AllCategoryResponse;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<IdResponse>> editCategory(@PathVariable String storeId, @RequestBody CategoryRequest categoryRequest,
                                                                @PathVariable String categoryId) {
        return categoryService.editCategory(categoryRequest, storeId, categoryId);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<IdResponse>> deleteCategory(@PathVariable String storeId, @PathVariable String categoryId) {
        return categoryService.deleteCategory(storeId, categoryId);
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryModel>> getCategory(@PathVariable String storeId) {
        return categoryService.getCategory(storeId);
    }

    @GetMapping("/category/all")
    public ResponseEntity<List<AllCategoryResponse>> getAllCategory(@PathVariable String storeId) {
        return categoryService.getAllCategory(storeId);
    }



}
