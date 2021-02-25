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

    private String subCategoryId;

    private String dokaneeCategory;

    private String shopId;

    private String slug;

    private String size;

    private String color;

    private String quantity;

    private String inStock;

    private String isFeatured;

    private String currentPrice;

    private String buyingPrice;

    private String regularPrice;

    private String vat;

    private String sku;

    private String metaTag;

    private String description;
}
