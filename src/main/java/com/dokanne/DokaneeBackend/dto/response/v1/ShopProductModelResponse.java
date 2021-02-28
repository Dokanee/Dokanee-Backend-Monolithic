package com.dokanne.DokaneeBackend.dto.response.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ShopProductModelResponse {
    private String productName;

    private String categoryId;

    private String subCategoryId;

    private String dokaneeCategory;

    private String storeId;

    private String slug;

    private String size;

    private String color;

    private String inStock;

    private String isFeatured;

    private Integer currentPrice;

    private Integer regularPrice;

    private Integer vat;

    private String sku;

    private String metaTag;

    private List<String> images;

    private String description;
}
