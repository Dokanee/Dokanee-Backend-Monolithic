package com.dokanne.DokaneeBackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.request.ProductAddRequest;
import com.dokanne.DokaneeBackend.dto.response.ErrorMassage;
import com.dokanne.DokaneeBackend.dto.response.MassageResponse;
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

    public ResponseEntity addProduct(ProductAddRequest proAddReq) {
        try {
            boolean storeIdAuth = userUtils.isStoreIdAuth(proAddReq.getStoreId());

            if (storeIdAuth) {
                ProductModel productModel = new ProductModel(
                        UUID.randomUUID().toString(), proAddReq.getStoreId(), proAddReq.getCategoryId(), proAddReq.getSubCategoryId(),
                        proAddReq.getProductName(), proAddReq.getBrand(), proAddReq.getSlug(), proAddReq.getSku(), proAddReq.getSellPrice(),
                        proAddReq.getDiscountPrice(), proAddReq.getQuantity(), proAddReq.getWeight(), proAddReq.getWeightUnit(),
                        proAddReq.getTypes(), proAddReq.getSize(), proAddReq.getColour(), proAddReq.isReturnable(),
                        proAddReq.getAllowMaxQtyToBuy(), proAddReq.getShortDescription(), proAddReq.getDescription(), proAddReq.isInStock(),
                        proAddReq.isFeatured(), proAddReq.getMetaKeywords(), proAddReq.getMetaDescription(), proAddReq.getTag()
                );
                productRepository.save(productModel);
                MassageResponse massageResponse = new MassageResponse(productModel.getProductName() + " is saved Successful with id " + productModel.getProductId(), 201);
                return new ResponseEntity(massageResponse, HttpStatus.CREATED);

            } else {
                MassageResponse massageResponse = new MassageResponse("StoreId Not Authenticated", 401);
                return new ResponseEntity(massageResponse, HttpStatus.UNAUTHORIZED);

            }

        } catch (Exception e) {
            ErrorMassage errorMassage = new ErrorMassage(e.toString());
            return new ResponseEntity(errorMassage, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity getProductListByCategory(String categoryId, String storeId) {

        boolean storeIdAuth = userUtils.isStoreIdAuth(storeId);

        boolean storeIdAndCategoryIdAuth = userUtils.isCategoryIdAndStoreIdAuth(storeId, categoryId);
        if (storeIdAuth && storeIdAndCategoryIdAuth) {
            Optional<List<ProductModel>> productModelListOptional = productRepository.findAllByStoreIdAndCategoryId(storeId, categoryId);

            if (productModelListOptional.isPresent()) {
                List<ProductModel> productModelList = productModelListOptional.get();
                MassageResponse massageResponse = new MassageResponse("Products found",productModelList, 200);
                return new ResponseEntity(massageResponse, HttpStatus.OK);
            } else {
                MassageResponse massageResponse = new MassageResponse("Products not found", 204);
                return new ResponseEntity(massageResponse, HttpStatus.NO_CONTENT);
            }

        } else {
            MassageResponse massageResponse = new MassageResponse("StoreId and CategoryId is not matched", 401);
            return new ResponseEntity(massageResponse, HttpStatus.UNAUTHORIZED);
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


                return new ResponseEntity("{\"status\":\"OK\"}", HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<String>("Wrong", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            MassageResponse massageResponse = new MassageResponse("Not Authenticated to Perform this Operation", 401);
            return new ResponseEntity(massageResponse, HttpStatus.UNAUTHORIZED);
        }
    }


}
