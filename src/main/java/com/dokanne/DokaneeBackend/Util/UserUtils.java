package com.dokanne.DokaneeBackend.Util;

import com.dokanne.DokaneeBackend.jwt.dto.response.OwnerProfileResponse;
import com.dokanne.DokaneeBackend.jwt.model.Role;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.product.v1.ProfileModel;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.ProductRepository;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor

@Service
public class UserUtils {

    private final ProfileRepository opRepo;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public boolean isStoreIdAuth(String storeId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();
        return storeList.contains(storeId);
    }

    public boolean isCategoryIdAndStoreIdAuth(String storeId, String categoryId) {
        Optional<CategoryModel> categoryModelOptional = categoryRepository.findByStoreIdAndCategoryId(storeId, categoryId);
        return categoryModelOptional.isPresent();
    }

    List<String> getStoreStringIds(List<StoreIds> storeIdsList) {
        List<String> storeIdsString = new ArrayList();
        System.out.println(storeIdsList.isEmpty());
        for (StoreIds storeIds : storeIdsList) {
            storeIdsString.add(storeIds.getStoreId());
        }

        return storeIdsString;
    }

    public OwnerProfileResponse getAuthUserInfo() {
        Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OwnerProfileResponse response;
        if (authUser instanceof UserDetails) {
            String username = ((UserDetails) authUser).getUsername();
            ProfileModel data = opRepo.findByUserName(username);
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

    public boolean authProduct(String id, String productId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();
        if(storeList.contains(id)) {
            return productRepository.findByStoreIdAndProductId(id, productId).isPresent();
        }
        else {
            return false;
        }

    }
}
