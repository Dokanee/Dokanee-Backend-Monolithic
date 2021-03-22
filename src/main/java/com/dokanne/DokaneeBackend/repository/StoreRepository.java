package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.StoreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreModel, String> {
    List<StoreModel> findAllByOwnerId(String ownerId);

    Optional<StoreModel> findBySubDomainName(String subDomainName);

    Optional<StoreModel> findByDomainName(String domainName);

}
