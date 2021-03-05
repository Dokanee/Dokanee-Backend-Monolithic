package com.dokanne.DokaneeBackend.service.v2;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.request.product.v2.ProductAddRequestV2;
import com.dokanne.DokaneeBackend.dto.response.ApiResponse;
import com.dokanne.DokaneeBackend.dto.response.MessageIdResponse;
import com.dokanne.DokaneeBackend.dto.response.ProductResponse;
import com.dokanne.DokaneeBackend.dto.response.v2.ProductPageResponseV2;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.ProductModel;
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
import java.util.*;

@AllArgsConstructor
@Service
public class ProductServiceV2 {

    private final ProductRepositoryV1_1 productRepositoryV11;
    private final CategoryRepository categoryRepository;
    private final UserUtils userUtils;
    private final ProductRepositoryV2 productRepositoryV2;


    public ResponseEntity<ProductResponse> getProductByCategory(String categoryId, String storeId, int pageNo, int pageSize) {
        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);
        boolean storeIdAndCategoryIdAuth = userUtils.isCategoryIdAndStoreIdAuth(storeId, categoryId);

        if (storeIdAuth && storeIdAndCategoryIdAuth) {
            CategoryModel categoryModel = null;

            Optional<CategoryModel> categoryModelOptional = categoryRepository.findById(categoryId);
            if (categoryModelOptional.isPresent()) {
                categoryModel = categoryModelOptional.get();
            }

            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<ProductModel> productModelPage = productRepositoryV11.findAllByStoreIdAndCategoryId(storeId, categoryId, pageable);

            assert categoryModel != null;
            ProductResponse productResponse = ProductResponse.builder()
                    .categoryId(categoryId)
                    .categoryName(categoryModel.getCategoryName())
                    .pageNo(pageNo)
                    .pageSize(productModelPage.getNumberOfElements())
                    .total(productModelPage.getTotalElements())
                    .products(productModelPage.getContent())
                    .build();

            return new ResponseEntity<>(productResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with Store Id or Category Id");
        }

    }

    public ResponseEntity<MessageIdResponse> addProduct(ProductAddRequestV2 addRequestV2, String storeId) {
        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);
        boolean storeIdAndCategoryIdAuth = userUtils.isCategoryIdAndStoreIdAuth(storeId, addRequestV2.getCategoryId());

        if (storeIdAuth && storeIdAndCategoryIdAuth) {
            String productId = UUID.randomUUID().toString();
            List<String> images = new ArrayList<>();

            String productSlug = addRequestV2.getProductName().toLowerCase().replace(" ", "-");
            productSlug = productSlug + "-" + storeId.substring(0, 7) + "-" + productId.substring(0, 7);

            ProductModelV2 productModelV2 = new ProductModelV2(productId, addRequestV2.getProductName(), addRequestV2.getCategoryId(), addRequestV2.getCategorySlug(),
                    addRequestV2.getSubCategoryId(), addRequestV2.getSubCategorySlug(), addRequestV2.getDokaneeCategory(), storeId, addRequestV2.getSubDomain(), productSlug,
                    addRequestV2.getSize(), addRequestV2.getColor(), addRequestV2.getQuantity(), addRequestV2.getInStock(), addRequestV2.getIsFeatured(),
                    addRequestV2.getCurrentPrice(), addRequestV2.getBuyingPrice(), addRequestV2.getRegularPrice(), addRequestV2.getVat(), addRequestV2.getSku(),
                    addRequestV2.getMetaTag(), images, addRequestV2.getDescription());

            productRepositoryV2.save(productModelV2);

            return new ResponseEntity(new MessageIdResponse("Product Added Successful", productId), HttpStatus.CREATED);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with Store Id or Category Id");
        }
    }

    public ResponseEntity<ProductPageResponseV2> getProductList(int pageNo, int pageSize, String storeId, String categoryId, String subCategoryId, String productName, String price) {

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


        return new ResponseEntity<>(responseV2, HttpStatus.OK);

    }


    public ResponseEntity<ApiResponse<ProductPageResponseV2>> getProductListTest(int pageNo, int pageSize, String storeId, String categoryId, String subCategoryId, String productName, String price) {

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

    public ResponseEntity uploadImage(MultipartFile[] aFile, String productId, String storeId) {
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
                    return new ResponseEntity<>("No File Found", HttpStatus.BAD_REQUEST);
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


                return new ResponseEntity<>("OK", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Upload failed\n" + e.toString(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("You are Not Authenticated to Perform this Operation", HttpStatus.UNAUTHORIZED);
        }
    }
}
