package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryModel, String> {
    List<CategoryModel> findAllByStoreId(String storeId);

    Optional<CategoryModel> findByStoreIdAndCategoryId(String storeId, String categoryId);
}
