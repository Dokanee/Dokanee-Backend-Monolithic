package com.dokanne.DokaneeBackend.dto.response;

import com.dokanne.DokaneeBackend.model.ProductModel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse {
    String categoryId;

    String categoryName;

    int pageSize;

    int pageNo;

    long total;

    List<ProductModel> products;
}
