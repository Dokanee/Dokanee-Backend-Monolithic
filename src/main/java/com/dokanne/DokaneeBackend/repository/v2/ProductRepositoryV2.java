package com.dokanne.DokaneeBackend.repository.v2;

import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepositoryV2 extends JpaRepository<ProductModelV2, String> {

    Optional<ProductModelV2> getAllByStoreId(String storeId);
}
