package com.dokanne.DokaneeBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table
public class CategoryModel {
    @Id
    String categoryId;

    String categoryIcon;

    String storeId;

    String subDomain;

    String categoryName;

    String slug;

    @OneToMany(cascade = CascadeType.ALL)
    List<SubCategoryModel> subCategoryModels;
}
