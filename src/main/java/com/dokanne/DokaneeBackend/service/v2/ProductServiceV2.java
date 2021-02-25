package com.dokanne.DokaneeBackend.service.v2;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.request.product.v2.ProductAddRequestV2;
import com.dokanne.DokaneeBackend.dto.response.MessageIdResponse;
import com.dokanne.DokaneeBackend.dto.response.ProductResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.ProductModel;
import com.dokanne.DokaneeBackend.model.product.v2.ProductModelV2;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV1_1;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV2;
import com.dokanne.DokaneeBackend.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ProductServiceV2 {

    private final ProductRepositoryV1_1 productRepositoryV11;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
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

            ProductModelV2 productModelV2 = new ProductModelV2(productId, addRequestV2.getProductName(), addRequestV2.getCategoryId(),
                    addRequestV2.getSubCategoryId(), addRequestV2.getDokaneeCategory(), addRequestV2.getShopId(), addRequestV2.getSlug(),
                    addRequestV2.getSize(), addRequestV2.getColor(), addRequestV2.getQuantity(), addRequestV2.getInStock(), addRequestV2.getIsFeatured(),
                    addRequestV2.getCurrentPrice(), addRequestV2.getBuyingPrice(), addRequestV2.getRegularPrice(), addRequestV2.getVat(), addRequestV2.getSku(),
                    addRequestV2.getMetaTag(), images, addRequestV2.getDescription());

            productRepositoryV2.save(productModelV2);

            return new ResponseEntity(new MessageIdResponse("Product Added Successful", productId), HttpStatus.CREATED);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Problem with Store Id or Category Id");
        }
    }

}
