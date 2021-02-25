package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.product.v1.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileModel, String> {
    ProfileModel findByUserName(String userName);

//    Optional< List<ProfileModel> > findByStoreIds(List<StoreIds> storeIds);

    Optional<ProfileModel> findByEmail(String email);

    Optional<List<ProfileModel>> findAllByStoreIdsContains(String id);

    @Query(value = "SELECT * FROM profile_model where owner_id in " +
            "(SELECT profile_model_owner_id from profile_model_store_ids " +
            "where store_ids = :storeId)",
            nativeQuery = true)
    Optional<List<ProfileModel>> findByStoreIdFromProfile(@Param("storeId") String storeId);

    //Optional<ProfileModel> findAllByStoreIdsIn
}
