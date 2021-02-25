package com.dokanne.DokaneeBackend.controller.v2;

import com.dokanne.DokaneeBackend.dto.request.product.v2.ProductAddRequestV2;
import com.dokanne.DokaneeBackend.dto.response.MessageIdResponse;
import com.dokanne.DokaneeBackend.dto.response.ProductResponse;
import com.dokanne.DokaneeBackend.service.v2.ProductServiceV2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//, @RequestHeader(name = "Authorization") String token

    @GetMapping("/product")
    public ResponseEntity<ProductResponse> getProductByCategory(
            @RequestParam(required = true) String categoryId,
            @RequestHeader String storeId,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "100000", required = false) int pageSize
    ) {

        return productServiceV2.getProductByCategory(categoryId, storeId, pageNo, pageSize);
    }
}
