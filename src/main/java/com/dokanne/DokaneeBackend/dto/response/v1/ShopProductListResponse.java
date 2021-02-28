package com.dokanne.DokaneeBackend.dto.response.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ShopProductListResponse {
    int pageNo;

    int pageSize;

    boolean isLastPage;

    Long totalItem;

    int totalPage;

    List<ShopProductModelResponse> shopProductModelResponses;
}
