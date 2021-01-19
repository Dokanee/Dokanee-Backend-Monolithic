package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.request.ProductAddRequest;
import com.dokanne.DokaneeBackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard/store/{storeId}")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/product")
    public ResponseEntity addProduct(@PathVariable String storeId, @RequestBody ProductAddRequest productAddRequest) {
        return productService.addProduct(productAddRequest, storeId);
    }

    @PutMapping("/product")
    public ResponseEntity editProduct(@PathVariable String storeId, @RequestBody ProductAddRequest productAddRequest,
                                      @RequestParam String productId) {
        return productService.editProduct(productAddRequest, storeId, productId);
    }

    @GetMapping("/product")
    public ResponseEntity getProductByCategory(@RequestParam(required = true) String categoryId, @PathVariable String storeId) {
        return productService.getProductListByCategory(categoryId, storeId);
    }


    @PostMapping(value = "/product/image/{productId}")
    public ResponseEntity postImage(@RequestParam(value = "image", required = true) MultipartFile[] aFile,
                                    @PathVariable String productId, @PathVariable String storeId) {
        return productService.uploadImage(aFile, productId, storeId);
    }

    @GetMapping("/products")
    public ResponseEntity getAllProducts(@RequestParam int pageNo,
                                         @RequestParam int pageSize, @PathVariable String storeId) {
        return productService.getAllProducts(pageNo, pageSize, storeId);
    }

}
