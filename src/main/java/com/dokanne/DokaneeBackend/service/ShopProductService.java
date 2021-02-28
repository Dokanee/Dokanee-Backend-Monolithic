package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopProductListResponse;
import com.dokanne.DokaneeBackend.dto.response.v1.ShopProductModelResponse;
import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV1_1;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV2;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ShopProductService {

    private final ProductRepositoryV1_1 productRepositoryV11;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    private final UserUtils userUtils;
    private final ProductRepositoryV2 productRepositoryV2;

    public ResponseEntity<ShopProductListResponse> getProductList(int pageNo, int pageSize, String storeId, String categoryId, String subCategoryId, String productName, String priceSort) {
        ProductModelV2 example = ProductModelV2.builder()
                .storeId(storeId)
                .categoryId(categoryId)
                .subCategoryId(subCategoryId)
                .productName(productName)
                .build();


        Pageable pageable;
        if (priceSort != null) {
            Sort sort;
            if (priceSort.equalsIgnoreCase("asc")) {
                sort = Sort.by("regularPrice").ascending();
            } else {
                sort = Sort.by("regularPrice").descending();
            }
            pageable = PageRequest.of(pageNo, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNo, pageSize);
        }


        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withMatcher("productName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Page<ProductModelV2> productModelV2List = productRepositoryV2.findAll(Example.of(example, matcher), pageable);

        List<ShopProductModelResponse> shopProductModelResponseList = new ArrayList<>();

        for (ProductModelV2 productModelV2 : productModelV2List) {
            ShopProductModelResponse shopProductModelResponse = new ShopProductModelResponse(productModelV2.getProductName(),
                    productModelV2.getCategoryId(), productModelV2.getSubCategoryId(), productModelV2.getDokaneeCategory(),
                    productModelV2.getStoreId(), productModelV2.getSlug(), productModelV2.getSize(), productModelV2.getColor(),
                    productModelV2.getInStock(), productModelV2.getIsFeatured(), productModelV2.getCurrentPrice(),
                    productModelV2.getRegularPrice(), productModelV2.getVat(), productModelV2.getSku(), productModelV2.getMetaTag(),
                    productModelV2.getImages(), productModelV2.getDescription()
            );
            shopProductModelResponseList.add(shopProductModelResponse);
        }

        ShopProductListResponse responseV2 = new ShopProductListResponse(pageNo, pageSize,
                productModelV2List.isLast(), productModelV2List.getTotalElements(),
                productModelV2List.getTotalPages(), shopProductModelResponseList);


        return new ResponseEntity<>(responseV2, HttpStatus.OK);
    }
}
