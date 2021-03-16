package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.TemplateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateModel, String> {
    Optional<TemplateModel> findByStoreId(String storeId);

    Optional<TemplateModel> findBySubDomain(String subDomain);
}
