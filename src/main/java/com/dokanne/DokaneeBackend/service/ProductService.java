package com.dokanne.DokaneeBackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.request.ProductAddRequest;
import com.dokanne.DokaneeBackend.model.ProductModel;
import com.dokanne.DokaneeBackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@AllArgsConstructor

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreService storeService;
    private final UserUtils userUtils;

    public ResponseEntity<Object> addProduct(ProductAddRequest proAddReq, String storeId) {
        try {
            boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);

            if (storeIdAuth) {
                ProductModel productModel = new ProductModel(
                        UUID.randomUUID().toString(), storeId, proAddReq.getCategoryId(), proAddReq.getSubCategoryId(),
                        proAddReq.getProductName(), proAddReq.getBrand(), proAddReq.getSlug(), proAddReq.getSku(), proAddReq.getSellPrice(),
                        proAddReq.getDiscountPrice(), proAddReq.getQuantity(), proAddReq.getWeight(), proAddReq.getWeightUnit(),
                        proAddReq.getTypes(), proAddReq.getSize(), proAddReq.getColour(), proAddReq.isReturnable(),
                        proAddReq.getAllowMaxQtyToBuy(), proAddReq.getShortDescription(), proAddReq.getDescription(), proAddReq.isInStock(),
                        proAddReq.isFeatured(), proAddReq.getMetaKeywords(), proAddReq.getMetaDescription(), proAddReq.getTag()
                );
                productRepository.save(productModel);

                return new ResponseEntity<>(productModel.getProductName() + " is saved Successful with id " + productModel.getProductId(), HttpStatus.CREATED);

            } else {
                return new ResponseEntity<>("Store is not authenticated", HttpStatus.UNAUTHORIZED);

            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity<Object> getProductListByCategory(String categoryId, String storeId) {

        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);

        boolean storeIdAndCategoryIdAuth = userUtils.isCategoryIdAndStoreIdAuth(storeId, categoryId);
        if (storeIdAuth && storeIdAndCategoryIdAuth) {
            Optional<List<ProductModel>> productModelListOptional = productRepository.findAllByStoreIdAndCategoryId(storeId, categoryId);

            if (productModelListOptional.isPresent()) {
                List<ProductModel> productModelList = productModelListOptional.get();

                return new ResponseEntity<>(productModelList, HttpStatus.OK);
            } else {

                return new ResponseEntity<>("Products not found", HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>("Store is not authenticated", HttpStatus.UNAUTHORIZED);
        }

    }


    public ResponseEntity uploadImage(MultipartFile[] aFile, String productId, String id) {
        boolean authProduct = userUtils.authProduct(id, productId);

        if (authProduct) {
            List<String> photoLinksList = new ArrayList<>();
            Cloudinary c = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "to-let-app",
                    "api_key", "111257839862595",
                    "api_secret", "7H1QY2G1W6FVQQ3envantRuJz4c"));

            try {
                Optional<ProductModel> productModelOptional = productRepository.findById(productId);

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
                ProductModel productModel = productModelOptional.get();
                productModel.setImageLink(photoLinksList);


                productRepository.save(productModel);


                return new ResponseEntity<>("OK", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Upload failed\n" + e.toString(), HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>("Not Authenticated to Perform this Operation", HttpStatus.UNAUTHORIZED);
        }
    }


}
