package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.StoreModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreModel, String> {
}
