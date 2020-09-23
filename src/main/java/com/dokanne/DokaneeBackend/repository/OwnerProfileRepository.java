package com.dokanne.DokaneeBackend.repository;

import com.dokanne.DokaneeBackend.model.OwnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerProfileRepository extends JpaRepository<OwnerProfile, String> {
    OwnerProfile findByUserName(String userName);
}
