package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.EditSubCategoryRequest;
import com.dokanne.DokaneeBackend.dto.request.SubCategoryRequest;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.service.SubCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard/store/{storeId}")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;


    @PostMapping("/subCategory")
    public ResponseEntity addSubCategory(@PathVariable String storeId, @RequestBody SubCategoryRequest subCategoryRequest) {
        return subCategoryService.addSubCategory(subCategoryRequest, storeId);
    }

    @PutMapping("/subCategory/{subCategoryId}")
    public ResponseEntity<ApiResponse<IdResponse>> editSubCategory(@PathVariable String storeId, @RequestBody EditSubCategoryRequest editSubCategoryRequest,
                                                                   @PathVariable String subCategoryId) {
        return subCategoryService.edtSubCategory(editSubCategoryRequest, storeId, subCategoryId);
    }

    @DeleteMapping("/subCategory/{subCategoryId}")
    public ResponseEntity<ApiResponse<IdResponse>> deleteSubCategory(@PathVariable String storeId, @PathVariable String subCategoryId) {
        return subCategoryService.edtSubCategory(storeId, subCategoryId);
    }
}
