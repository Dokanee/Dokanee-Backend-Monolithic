package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileModel, String> {
    ProfileModel findByUserName(String userName);

//    Optional< List<ProfileModel> > findByStoreIds(List<StoreIds> storeIds);

    Optional<ProfileModel> findByEmail(String email);
}
