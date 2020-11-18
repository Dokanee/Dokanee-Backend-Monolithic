package com.dokanne.DokaneeBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubCategoryRequest {
    String subCategoryName;
    String categoryId;
    String slug;
}
