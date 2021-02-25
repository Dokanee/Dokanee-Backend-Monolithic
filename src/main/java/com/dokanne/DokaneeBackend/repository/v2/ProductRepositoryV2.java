package com.dokanne.DokaneeBackend.repository.v2;

import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositoryV2 extends JpaRepository<ProductModelV2, String> {
}
