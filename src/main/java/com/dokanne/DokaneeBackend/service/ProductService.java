package com.dokanne.DokaneeBackend.service;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.request.ProductAddRequest;
import com.dokanne.DokaneeBackend.dto.response.ErrorMassage;
import com.dokanne.DokaneeBackend.dto.response.MassageResponse;
import com.dokanne.DokaneeBackend.model.ProductModel;
import com.dokanne.DokaneeBackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreService storeService;
    private final UserUtils userUtils;

    public ResponseEntity addProduct(ProductAddRequest proAddReq) {
        try {
            //boolean storeIdAuth = UserUtils.isStoreIdAuth(proAddReq.getStoreId());
            boolean storeIdAuth = true;

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

                return new ResponseEntity(productModel.getProductName() + " is saved Successful with id " + productModel.getProductId(), HttpStatus.CREATED);

            } else {
                ErrorMassage errorMassage = new ErrorMassage("StoreId Not Authenticated");
                return new ResponseEntity(errorMassage, HttpStatus.UNAUTHORIZED);

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

                return new ResponseEntity(productModelList, HttpStatus.OK);
            } else {
                MassageResponse massageResponse = new MassageResponse("Products not found");
                return new ResponseEntity(massageResponse, HttpStatus.NO_CONTENT);
            }

        } else {
            MassageResponse massageResponse = new MassageResponse("StoreId and CategoryId is not matched");
            return new ResponseEntity(massageResponse, HttpStatus.UNAUTHORIZED);
        }

    }


}
//Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoamhhYmliMjRAZ21haWwuY29tMDE1MTUyMTI2ODciLCJzY29wZXMiOiJPV05FUiIsImlhdCI6MTYwMTMyNjU4NCwiZXhwIjoxNjAzNDI2NTg0fQ.aEcg3ZuAjVEI2EIUUYhdriKtp_YtVQDdigFSO3BnQqND4jVXH0ihThEK_OXAsS9wUztJ3CSPRRd_tda3Ike-EQ