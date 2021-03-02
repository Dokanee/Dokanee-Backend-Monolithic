package com.dokanne.DokaneeBackend.dto.response.v1;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopCategory {
    String categoryName;

    String slug;

    List<ShopSubCategory> subCategoryList;

}
