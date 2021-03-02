package com.dokanne.DokaneeBackend.dto.request.product.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductAddRequestV2 {

    private String productName;

    private String categoryId;

    private String categorySlug;

    private String subCategoryId;

    private String subCategorySlug;

    private String dokaneeCategory;

    private String storeId;

    private String subDomain;

    private String size;

    private String color;

    private String quantity;

    private String inStock;

    private String isFeatured;

    private Integer currentPrice;

    private Integer buyingPrice;

    private Integer regularPrice;

    private Integer vat;

    private String sku;

    private String metaTag;

    private String description;
}
