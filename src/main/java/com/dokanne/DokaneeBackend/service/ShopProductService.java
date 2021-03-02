package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.response.v1.*;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.model.SubCategoryModel;
import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV2;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ShopProductService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ProductRepositoryV2 productRepositoryV2;

    public ResponseEntity<ShopProductListResponse> getProductList(int pageNo, int pageSize, String subDomain, String categorySlug, String subCategorySlug, String productName, String priceSort) {
        ProductModelV2 example = ProductModelV2.builder()
                .subDomain(subDomain)
                .categorySlug(categorySlug)
                .subCategorySlug(subCategorySlug)
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
                    productModelV2.getCategorySlug(), productModelV2.getSubCategorySlug(), productModelV2.getDokaneeCategory(),
                    productModelV2.getSubDomain(), productModelV2.getSlug(), productModelV2.getSize(), productModelV2.getColor(),
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

    public ResponseEntity<ShopCategoryListResponse> getCategoryList(String subDomain) {
        Optional<StoreModel> storeModelOptional = storeRepository.findBySubDomainName(subDomain);

        if (storeModelOptional.isPresent()) {
            StoreModel storeModel = storeModelOptional.get();


            List<CategoryModel> categoryModels = categoryRepository.findAllByStoreId(storeModel.getStoreId());

            List<ShopCategory> shopCategories = new ArrayList<>();


            for (CategoryModel categoryModel : categoryModels) {
                List<ShopSubCategory> shopSubCategories = new ArrayList<>();
                for (SubCategoryModel subCategoryModel : categoryModel.getSubCategoryModels()) {
                    ShopSubCategory shopSubCategory = new ShopSubCategory(subCategoryModel.getSubCategoryName(),
                            subCategoryModel.getSlug());
                    shopSubCategories.add(shopSubCategory);
                }

                ShopCategory shopCategory = new ShopCategory(categoryModel.getCategoryName(),
                        categoryModel.getSlug(), shopSubCategories);

                shopCategories.add(shopCategory);
            }

            return new ResponseEntity<>(new ShopCategoryListResponse(storeModel.getSubDomainName(),
                    storeModel.getStoreName(), shopCategories), HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store Not Found");
        }

    }
}
