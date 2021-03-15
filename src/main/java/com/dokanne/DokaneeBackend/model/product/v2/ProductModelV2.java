package com.dokanne.DokaneeBackend.model.product.v2;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Data
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

    private String creationTime;

    private String badge;

    private String categoryId;

    private String categorySlug;

    private String subCategoryId;

    private String subCategorySlug;

    private String dokaneeCategory;

    private String storeId;

    private String subDomain;

    private String slug;

    private String size;

    private String color;

    private String quantity;

    private String inStock;

    private String isFeatured;

    private Integer currentPrice;

    private Integer buyingPrice;

    private Integer regularPrice;

    private Double vat;

    private String sku;

    private String metaTag;

    @ElementCollection
    private List<String> images;

    @Column(columnDefinition = "Text")
    private String description;
}
