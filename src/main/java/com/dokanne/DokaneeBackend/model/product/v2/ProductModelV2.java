package com.dokanne.DokaneeBackend.model.product.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class ProductModelV2 {
    @Id
    private String id;

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

    @ElementCollection
    private List<String> images;

    @Column(columnDefinition = "Text")
    private String description;
}
