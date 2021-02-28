package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.response.v1.ShopProductListResponse;
import com.dokanne.DokaneeBackend.service.ShopProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/v1/shop/products")
public class ShopProductController {

    private final ShopProductService shopProductService;

    @GetMapping("/")
    public ResponseEntity<ShopProductListResponse> getProductList(@RequestHeader String storeId,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "20") int pageSize,
                                                                  @RequestParam(required = false) String categoryId,
                                                                  @RequestParam(required = false) String subCategoryId,
                                                                  @RequestParam(required = false) String productName,
                                                                  @RequestParam(required = false) String priceSort
    ) {
        return shopProductService.getProductList(pageNo, pageSize, storeId, categoryId, subCategoryId, productName, priceSort);
    }

}
