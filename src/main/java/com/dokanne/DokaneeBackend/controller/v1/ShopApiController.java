package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.response.shopResponse.ShopStoreResponse;
import com.dokanne.DokaneeBackend.dto.response.shopResponse.ShopTemplateResponse;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopCategoryListResponse;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopProductListResponse;
import com.dokanne.DokaneeBackend.service.ShopApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/v1/shop/{subDomain}")
public class ShopApiController {

    private final ShopApiService shopApiService;

    @GetMapping("/products")
    public ResponseEntity<ShopProductListResponse> getProductList(@PathVariable String subDomain,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "20") int pageSize,
                                                                  @RequestParam(required = false) String categorySlug,
                                                                  @RequestParam(required = false) String subCategorySlug,
                                                                  @RequestParam(required = false) String productName,
                                                                  @RequestParam(required = false) String priceSort
    ) {
        return shopApiService.getProductList(pageNo, pageSize, subDomain, categorySlug, subCategorySlug, productName, priceSort);
    }

    @GetMapping("/categories")
    public ResponseEntity<ShopCategoryListResponse> getCategoryList(@PathVariable String subDomain) {
        return shopApiService.getCategoryList(subDomain);
    }

    @GetMapping("/template")
    public ResponseEntity<ApiResponse<ShopTemplateResponse>> getTemplateInfo(@PathVariable String subDomain) {
        return shopApiService.getTemplateInfo(subDomain);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ShopStoreResponse>> getStoreInfo(@PathVariable String subDomain) {
        return shopApiService.getStoreInfo(subDomain);
    }

}
