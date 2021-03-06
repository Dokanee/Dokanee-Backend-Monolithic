package com.dokanne.DokaneeBackend.service.v2;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.product.v2.ProductAddRequestV2;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.dto.response.MessageIdResponse;
import com.dokanne.DokaneeBackend.dto.response.v2.ProductPageResponseV2;
import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV1_1;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV2;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;

@AllArgsConstructor
@Service
public class ProductServiceV2 {

    private final ProductRepositoryV1_1 productRepositoryV11;
    private final CategoryRepository categoryRepository;
    private final UserUtils userUtils;
    private final ProductRepositoryV2 productRepositoryV2;

    public ResponseEntity<MessageIdResponse> addProduct(ProductAddRequestV2 addRequestV2, String storeId) {
        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);
        boolean storeIdAndCategoryIdAuth = userUtils.isCategoryIdAndStoreIdAuth(storeId, addRequestV2.getCategoryId());

        if (storeIdAuth && storeIdAndCategoryIdAuth) {
            String productId = UUID.randomUUID().toString();
            List<String> images = new ArrayList<>();

            String productSlug = addRequestV2.getProductName().toLowerCase().replace(" ", "-");
            productSlug = productSlug + "-" + storeId.substring(0, 7) + "-" + productId.substring(0, 7);

            long timestamp = new Timestamp(System.currentTimeMillis()).getTime();

            ProductModelV2 productModelV2 = new ProductModelV2(productId, addRequestV2.getProductName(),
                    String.valueOf(timestamp), addRequestV2.getBadge(), addRequestV2.getCategoryId(),
                    addRequestV2.getCategorySlug(), addRequestV2.getSubCategoryId(), addRequestV2.getSubCategorySlug(),
                    addRequestV2.getDokaneeCategory(), storeId, addRequestV2.getSubDomain(), productSlug, addRequestV2.getSize(),
                    addRequestV2.getColor(), addRequestV2.getQuantity(), addRequestV2.getInStock(), addRequestV2.getIsFeatured(),
                    addRequestV2.getCurrentPrice(), addRequestV2.getBuyingPrice(), addRequestV2.getRegularPrice(),
                    addRequestV2.getVat(), addRequestV2.getSku(), addRequestV2.getMetaTag(), images, addRequestV2.getDescription());

            productRepositoryV2.save(productModelV2);

            return new ResponseEntity(new MessageIdResponse("Product Added Successful", productId), HttpStatus.CREATED);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with Store Id or Category Id");
        }
    }

    public ResponseEntity<ApiResponse<ProductPageResponseV2>> getProductList(int pageNo, int pageSize, String storeId, String categoryId, String subCategoryId, String productName, String price) {

        ProductModelV2 example = ProductModelV2.builder()
                .storeId(storeId)
                .categoryId(categoryId)
                .subCategoryId(subCategoryId)
                .productName(productName)
                .build();


        Pageable pageable;
        if (price != null) {
            Sort sort;
            if (price.toLowerCase().equals("asc")) {
                sort = Sort.by("regularPrice").ascending();
            } else {
                sort = Sort.by("regularPrice").descending();
            }
            pageable = PageRequest.of(pageNo, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNo, pageSize);
        }


        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withMatcher("productName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Page<ProductModelV2> productModelV2List = productRepositoryV2.findAll(Example.of(example, matcher), pageable);


        ProductPageResponseV2 responseV2 = new ProductPageResponseV2(pageNo, pageSize,
                productModelV2List.isLast(), productModelV2List.getTotalElements(),
                productModelV2List.getTotalPages(), productModelV2List.getContent());


        return new ResponseEntity<>(new ApiResponse<>(200, "OK", responseV2), HttpStatus.OK);

    }

    public ResponseEntity<ApiResponse<List<String>>> uploadImage(MultipartFile[] aFile, String productId, String storeId) {
        boolean authProduct = userUtils.authProductV2(storeId, productId);

        if (authProduct) {
            List<String> photoLinksList = new ArrayList<>();
            Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "to-let-app",
                    "api_key", "111257839862595",
                    "api_secret", "7H1QY2G1W6FVQQ3envantRuJz4c"));

            try {
                Optional<ProductModelV2> productModelOptional = productRepositoryV2.findById(productId);

                if (aFile.length < 1) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No File Found");
                }

                for (MultipartFile mpFile : aFile) {
                    File f = Files.createTempFile("temp", mpFile.getOriginalFilename()).toFile();
                    mpFile.transferTo(f);
                    Map response = c.uploader().upload(f, ObjectUtils.emptyMap());
                    JSONObject json = new JSONObject(response);
                    String url = json.getString("url");

                    photoLinksList.add(url);
                }
                ProductModelV2 productModelV2 = productModelOptional.get();
                productModelV2.setImages(photoLinksList);

                productRepositoryV2.save(productModelV2);

                return new ResponseEntity<>(new ApiResponse<>(200, "Image Upload Successful", photoLinksList), HttpStatus.OK);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upload failed\n " + e.toString());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are Not Authenticated to Perform this Operation");
        }
    }

    public ResponseEntity<ApiResponse<IdResponse>> editProduct(ProductAddRequestV2 addRequestV2, String storeId, String productId) {
        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);
        boolean storeIdAndCategoryIdAuth = userUtils.isCategoryIdAndStoreIdAuth(storeId, addRequestV2.getCategoryId());

        if (storeIdAuth && storeIdAndCategoryIdAuth) {
            Optional<ProductModelV2> productModelV2Optional = productRepositoryV2.findById(productId);

            if (productModelV2Optional.isPresent()) {
                ProductModelV2 productModelV2Db = productModelV2Optional.get();

                ProductModelV2 productModelV2 = new ProductModelV2(productModelV2Db.getId(), addRequestV2.getProductName(),
                        productModelV2Db.getCreationTime(), addRequestV2.getBadge(), addRequestV2.getCategoryId(),
                        addRequestV2.getCategorySlug(), addRequestV2.getSubCategoryId(), addRequestV2.getSubCategorySlug(),
                        addRequestV2.getDokaneeCategory(), storeId, addRequestV2.getSubDomain(), productModelV2Db.getSlug(), addRequestV2.getSize(),
                        addRequestV2.getColor(), addRequestV2.getQuantity(), addRequestV2.getInStock(), addRequestV2.getIsFeatured(),
                        addRequestV2.getCurrentPrice(), addRequestV2.getBuyingPrice(), addRequestV2.getRegularPrice(),
                        addRequestV2.getVat(), addRequestV2.getSku(), addRequestV2.getMetaTag(), productModelV2Db.getImages(), addRequestV2.getDescription());

                productRepositoryV2.save(productModelV2);

                return new ResponseEntity<>(new ApiResponse<>(200, "Product Edit Successful",
                        new IdResponse(productModelV2Db.getId())), HttpStatus.OK);

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with Store Id or Category Id");
        }
    }

    public ResponseEntity<ApiResponse<IdResponse>> deleteProduct(String storeId, String productId) {
        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);

        if (storeIdAuth) {
            Optional<ProductModelV2> productModelV2Optional = productRepositoryV2.findById(productId);

            if (productModelV2Optional.isPresent()) {
                ProductModelV2 productModelV2Db = productModelV2Optional.get();

                productRepositoryV2.deleteById(productModelV2Db.getId());

                return new ResponseEntity<>(new ApiResponse<>(200, "Product Delete Successful",
                        new IdResponse(productModelV2Db.getId())), HttpStatus.OK);

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with Store Id");
        }
    }
}
