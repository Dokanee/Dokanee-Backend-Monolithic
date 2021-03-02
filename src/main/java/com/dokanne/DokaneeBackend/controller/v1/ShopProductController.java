package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.response.v1.ShopCategoryListResponse;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopProductListResponse;
import com.dokanne.DokaneeBackend.service.ShopProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/v1/shop/{subDomain}")
public class ShopProductController {

    private final ShopProductService shopProductService;

    @GetMapping("/products")
    public ResponseEntity<ShopProductListResponse> getProductList(@PathVariable String subDomain,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "20") int pageSize,
                                                                  @RequestParam(required = false) String categorySlug,
                                                                  @RequestParam(required = false) String subCategorySlug,
                                                                  @RequestParam(required = false) String productName,
                                                                  @RequestParam(required = false) String priceSort
    ) {
        return shopProductService.getProductList(pageNo, pageSize, subDomain, categorySlug, subCategorySlug, productName, priceSort);
    }

    @GetMapping("/categories")
    public ResponseEntity<ShopCategoryListResponse> getCategoryList(@PathVariable String subDomain) {
        return shopProductService.getCategoryList(subDomain);
    }

}
