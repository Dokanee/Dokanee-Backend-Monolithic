package com.dokanne.DokaneeBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AllCategoryResponse {
    String id;

    String categoryIcon;

    String name;
}
