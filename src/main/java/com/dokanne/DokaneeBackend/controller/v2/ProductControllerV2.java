package com.dokanne.DokaneeBackend.controller.v2;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.product.v2.ProductAddRequestV2;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.dto.response.MessageIdResponse;
import com.dokanne.DokaneeBackend.dto.response.v2.ProductPageResponseV2;
import com.dokanne.DokaneeBackend.service.v2.ProductServiceV2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/v2/dashboard/products")
public class ProductControllerV2 {

    private final ProductServiceV2 productServiceV2;

    @PostMapping("/")
    public ResponseEntity<MessageIdResponse> addProduct(@RequestBody ProductAddRequestV2 productAddRequestV2,
                                                        @RequestHeader String storeId) {
        return productServiceV2.addProduct(productAddRequestV2, storeId);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<ProductPageResponseV2>> getProductList(@RequestHeader String storeId,
                                                                             @RequestParam(defaultValue = "0") int pageNo,
                                                                             @RequestParam(defaultValue = "20") int pageSize,
                                                                             @RequestParam(required = false) String categoryId,
                                                                             @RequestParam(required = false) String subCategoryId,
                                                                             @RequestParam(required = false) String productName,
                                                                             @RequestParam(required = false) String priceSort
    ) {
        return productServiceV2.getProductList(pageNo, pageSize, storeId, categoryId, subCategoryId, productName, priceSort);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<IdResponse>> editProduct(@RequestBody ProductAddRequestV2 productAddRequestV2,
                                                               @RequestHeader String storeId, @PathVariable String productId) {
        return productServiceV2.editProduct(productAddRequestV2, storeId, productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<IdResponse>> deleteProduct(@RequestHeader String storeId, @PathVariable String productId) {
        return productServiceV2.deleteProduct(storeId, productId);
    }

    @PostMapping(value = "/{productId}/image")
    public ResponseEntity<ApiResponse<List<String>>> postImage(@RequestParam(value = "image", required = true) MultipartFile[] aFile,
                                                               @PathVariable String productId, @RequestHeader String storeId) {
        return productServiceV2.uploadImage(aFile, productId, storeId);
    }


}
