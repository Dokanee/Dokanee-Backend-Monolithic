package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CategoryResponse {
    String categoryId;

    String categoryName;

    String slug;

    List<String> subCategoryModels;
}
