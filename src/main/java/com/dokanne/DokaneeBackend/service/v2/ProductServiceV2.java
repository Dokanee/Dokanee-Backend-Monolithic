package com.dokanne.DokaneeBackend.service.v2;

import com.dokanne.DokaneeBackend.Util.UserUtils;
import com.dokanne.DokaneeBackend.dto.response.ProductResponse;
import com.dokanne.DokaneeBackend.model.CategoryModel;
import com.dokanne.DokaneeBackend.model.ProductModel;
import com.dokanne.DokaneeBackend.repository.CategoryRepository;
import com.dokanne.DokaneeBackend.repository.v2.ProductRepositoryV2;
import com.dokanne.DokaneeBackend.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceV2 {

    private final ProductRepositoryV2 productRepositoryV2;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    private final UserUtils userUtils;


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
            Page<ProductModel> productModelPage = productRepositoryV2.findAllByStoreIdAndCategoryId(storeId, categoryId, pageable);

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
            return new ResponseEntity<>(new ProductResponse(), HttpStatus.UNAUTHORIZED);
        }

    }
}
