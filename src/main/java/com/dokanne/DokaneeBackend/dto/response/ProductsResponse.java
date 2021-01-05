package com.dokanne.DokaneeBackend.dto.response;

import com.dokanne.DokaneeBackend.model.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductsResponse {
    int pageSize;
    int pageNo;
    int total;

    List<ProductModel> productModelList;
}
