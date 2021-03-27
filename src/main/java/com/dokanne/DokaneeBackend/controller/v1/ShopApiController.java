package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.response.PaginationResponse;
import com.dokanne.DokaneeBackend.dto.response.ShopStoreInfoResponse;
import com.dokanne.DokaneeBackend.dto.response.shopResponse.ShopStoreResponse;
import com.dokanne.DokaneeBackend.dto.response.shopResponse.ShopTemplateResponse;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopCategoryListResponse;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopProductListResponse;
import com.dokanne.DokaneeBackend.service.ShopApiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/v1/shop")
public class ShopApiController {

    private final ShopApiService shopApiService;

    @GetMapping("/{subDomain}/products")
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

    @GetMapping("/{subDomain}/categories")
    public ResponseEntity<ShopCategoryListResponse> getCategoryList(@PathVariable String subDomain) {
        return shopApiService.getCategoryList(subDomain);
    }

    @GetMapping("/{subDomain}/template")
    public ResponseEntity<ApiResponse<ShopTemplateResponse>> getTemplateInfo(@PathVariable String subDomain) {
        return shopApiService.getTemplateInfo(subDomain);
    }

    @GetMapping("/{subDomain}")
    public ResponseEntity<ApiResponse<ShopStoreResponse>> getStoreInfo(@PathVariable String subDomain) {
        return shopApiService.getStoreInfo(subDomain);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<List<ShopStoreInfoResponse>>>> getAllStore(@RequestParam(defaultValue = "0") int pageNo,
                                                                                                    @RequestParam(defaultValue = "20") int pageSize,
                                                                                                    @RequestParam(required = false) String storeName,
                                                                                                    @RequestParam(required = false) String storeCategory,
                                                                                                    @RequestParam(required = false) String upzila,
                                                                                                    @RequestParam(required = false) String zila,
                                                                                                    @RequestParam(required = false) int nameSort,
                                                                                                    @RequestParam(required = false) int creationTimeSort) {
        return shopApiService.getAllStore(pageNo, pageSize, storeName, storeCategory, upzila, zila, nameSort, creationTimeSort);
    }

}
