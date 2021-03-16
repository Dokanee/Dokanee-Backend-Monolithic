package com.dokanne.DokaneeBackend.Util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dokanne.DokaneeBackend.jwt.dto.response.OwnerProfileResponse;
import com.dokanne.DokaneeBackend.jwt.model.Role;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.StoreIds;
import com.dokanne.DokaneeBackend.model.product.v1.ProfileModel;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.ProductRepository;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV2;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@AllArgsConstructor

@Service
public class UserUtils {

    private final ProfileRepository opRepo;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductRepositoryV2 productRepositoryV2;

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

    public boolean authProduct(String storeId, String productId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();

        if (storeList.contains(storeId)) {
            return productRepository.findByStoreIdAndProductId(storeId, productId).isPresent();
        } else {
            System.out.println(2);
            return false;
        }

    }

    public boolean authProductV2(String storeId, String productId) {
        List<String> storeList = getAuthUserInfo().getStoreIds();

        if (storeList.contains(storeId)) {
            return productRepositoryV2.findByStoreIdAndId(storeId, productId).isPresent();
        } else {
            return false;
        }
    }

    public static List<String> uploadImage(MultipartFile[] aFile) throws Exception {

        List<String> photoLinksList = new ArrayList<>();
        Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "to-let-app",
                "api_key", "111257839862595",
                "api_secret", "7H1QY2G1W6FVQQ3envantRuJz4c"));

        try {
            if (aFile.length < 1) {
                throw new Exception("No File Found");
            }

            for (MultipartFile mpFile : aFile) {
                File f = Files.createTempFile("temp", mpFile.getOriginalFilename()).toFile();
                mpFile.transferTo(f);
                Map response = c.uploader().upload(f, ObjectUtils.emptyMap());
                JSONObject json = new JSONObject(response);
                String url = json.getString("url");

                photoLinksList.add(url);
            }

            return photoLinksList;
        } catch (Exception e) {
            throw new Exception("upload Failed" + e);
        }
    }
}

