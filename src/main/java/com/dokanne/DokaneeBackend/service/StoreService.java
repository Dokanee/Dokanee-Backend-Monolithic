package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.jwt.dto.response.OwnerProfileResponse;
import com.dokanne.DokaneeBackend.jwt.model.Role;
import com.dokanne.DokaneeBackend.model.OwnerProfile;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.repository.OwnerProfileRepository;
import com.dokanne.DokaneeBackend.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class StoreService {


    private final OwnerProfileRepository ownerProfileRepository;
    private final StoreRepository storeRepository;
    private final OwnerProfileRepository opRepo;

    public OwnerProfileResponse getAuthUserInfo() {

        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OwnerProfileResponse response;

        if (authUser instanceof UserDetails) {

            String username = ((UserDetails) authUser).getUsername();
            OwnerProfile data = opRepo.findByUserName(username);

            response = new OwnerProfileResponse("OK", data.getOwnerId(), data.getFirstName(), data.getLastName(), data.getEmail(), data.getPhone(), data.getAddress(), data.getStoreIds());

            return response;

        } else if (authUser instanceof UserDetails == false) {
            response = new OwnerProfileResponse();
            response.setMassage("Bearer Token Problem");
            return response;

        } else {
            String username = authUser.toString();

            System.out.println(username);
        }
        response = new OwnerProfileResponse();
        response.setMassage("Not Found");
        return response;

    }

    private Set<String> getRolesToString(Set<Role> roles2) {
        Set<String> roles = new HashSet<>();
        for (Role role : roles2) {

            roles.add(role.getName().toString());
        }
        return roles;
    }

    public String createStore(StoreRequest storeRequest) {

        String id = UUID.randomUUID().toString();
        StoreModel storeModel = new StoreModel(id, getAuthUserInfo().getOwnerId(), storeRequest.getStoreName(),
                storeRequest.getSubDomainName(), storeRequest.getStoreCategory(), storeRequest.getAddress(),
                storeRequest.getUpzila(), storeRequest.getZila(), storeRequest.getDivision());
        storeModel.setHavePhysicalStore(storeRequest.isHavePhysicalStore());

        storeRepository.save(storeModel);

        OwnerProfile ownerProfile = ownerProfileRepository.findById(getAuthUserInfo().getOwnerId()).get();

        List<StoreIds> storeIdsList = ownerProfile.getStoreIds();
        StoreIds storeIds = new StoreIds();
        storeIds.setStoreId(id);
        storeIdsList.add(storeIds);
        ownerProfile.setStoreIds(storeIdsList);

        ownerProfileRepository.save(ownerProfile);

        return id;
    }


}

