package com.dokanne.DokaneeBackend.repository.v2;

import com.dokanne.DokaneeBackend.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryV2 extends PagingAndSortingRepository<ProductModel, String> {

    Page<ProductModel> findAllByStoreIdAndCategoryId(String storeId, String categoryId, Pageable pageable);

}
