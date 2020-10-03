package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileModel, String> {
    ProfileModel findByUserName(String userName);
}
