package com.dokanne.DokaneeBackend.controller.v2;

import com.dokanne.DokaneeBackend.dto.response.ProductResponse;
import com.dokanne.DokaneeBackend.service.v2.ProductServiceV2;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/v2/dashboard/store")
public class ProductControllerV2 {

    private final ProductServiceV2 productServiceV2;

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
