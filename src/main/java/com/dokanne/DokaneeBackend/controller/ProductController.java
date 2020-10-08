package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.ProductAddRequest;
import com.dokanne.DokaneeBackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor

@RestController
@RequestMapping("/dashboard")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product/add")
    public ResponseEntity addProduct(@RequestBody ProductAddRequest productAddRequest) {
        return productService.addProduct(productAddRequest);
    }

    @GetMapping("/product")
    public ResponseEntity getProductByCategory(@RequestParam(required = true) String categoryId, @RequestParam(required = true) String storeId) {
        return productService.getProductListByCategory(categoryId, storeId);
    }

    @PostMapping(value = "/product/add/image")
    public ResponseEntity postImage(@RequestParam(value = "image", required = true) MultipartFile[] aFile,
                                    @RequestParam(required = true) String productId, @RequestParam(required = true) String storeId) {
        return productService.uploadImage(aFile, productId, storeId);
    }

}
