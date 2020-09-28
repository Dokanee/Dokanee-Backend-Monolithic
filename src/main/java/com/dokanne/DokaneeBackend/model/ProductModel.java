package com.dokanne.DokaneeBackend.model;

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
public class ProductModel {
    @Id
    private String productId;

    private String storeId;

    private String categoryId;

    private String subCategoryId;

    private String productName;

    private String brand;

    private String slug;

    private String sku;

    private int sellPrice;

    private int discountPrice;

    private int quantity;

    private int weight;

    private String weightUnit;

    private String types;

    private int size;

    private String colour;

    private boolean isReturnable;

    private int allowMaxQtyToBuy;

    private String shortDescription;

    private String description;

    private boolean isInStock;

    private boolean isFeatured;

    private String metaKeywords;

    private String metaDescription;

    private String tag;

    @ElementCollection
    @CollectionTable
    private List<String> imageLink;
}
