package com.dokanne.DokaneeBackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor

@Entity
@Table
public class ProductModel {
    @NonNull
    @Id
    private String productId;
    @NonNull
    private String storeId;
    @NonNull
    private String categoryId;
    @NonNull
    private String subCategoryId;
    @NonNull
    private String productName;
    @NonNull
    private String brand;
    @NonNull
    private String slug;
    @NonNull
    private String sku;
    @NonNull
    private int sellPrice;
    @NonNull
    private int discountPrice;
    @NonNull
    private int quantity;
    @NonNull
    private int weight;
    @NonNull
    private String weightUnit;
    @NonNull
    private String types;
    @NonNull
    private int size;
    @NonNull
    private String colour;
    @NonNull
    private boolean isReturnable;
    @NonNull
    private int allowMaxQtyToBuy;
    @NonNull
    private String shortDescription;
    @NonNull
    private String description;
    @NonNull
    private boolean isInStock;
    @NonNull
    private boolean isFeatured;
    @NonNull
    private String metaKeywords;
    @NonNull
    private String metaDescription;
    @NonNull
    private String tag;

    @ElementCollection
    @CollectionTable
    private List<String> imageLink;
}
