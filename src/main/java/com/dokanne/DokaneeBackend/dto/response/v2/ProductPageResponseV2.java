package com.dokanne.DokaneeBackend.dto.response.v2;

import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageResponseV2 {
    int pageNo;

    int pageSize;

    boolean isLastPage;

    Long totalItem;

    int totalPage;

    List<ProductModelV2> products;
}
