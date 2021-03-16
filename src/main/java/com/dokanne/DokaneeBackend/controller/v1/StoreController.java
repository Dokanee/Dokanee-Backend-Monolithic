package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.dto.response.StoreInfoResponse;
import com.dokanne.DokaneeBackend.model.StoreModel;
import com.dokanne.DokaneeBackend.service.StoreImageService;
import com.dokanne.DokaneeBackend.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard")
public class StoreController {

    private final StoreService storeService;
    private final StoreImageService storeImageService;

    @PostMapping("/store")
    public ResponseEntity<ApiResponse<IdResponse>> createStore(@RequestBody StoreRequest storeRequest) {
        return storeService.createStore(storeRequest);
    }

    @GetMapping("/store")
    public ResponseEntity<ApiResponse<List<StoreInfoResponse>>> getStoreInfo() {
        return storeService.getStoreInfo();
    }

    @PutMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<IdResponse>> editStoreInfo(@PathVariable String storeId, @RequestBody StoreRequest storeRequest) {
        return storeService.editStore(storeId, storeRequest);
    }

    @PostMapping("/store/logo/{storeId}")
    public ResponseEntity<ApiResponse<String>> uploadStoreLogo(@RequestParam(value = "image", required = true)
                                                                       MultipartFile aFile, @PathVariable String storeId) {
        return storeImageService.uploadStoreLogo(aFile, storeId);
    }

    @DeleteMapping("/store/logo/{storeId}")
    public ResponseEntity<ApiResponse<String>> deleteStoreLogo(@PathVariable String storeId) {
        return storeImageService.deleteStoreLogo(storeId);
    }

    @DeleteMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<IdResponse>> deleteStoreInfo(@PathVariable String storeId) {
        return storeService.deleteStore(storeId);
    }

    @GetMapping("/store/check")
    public ResponseEntity<Boolean> check(@RequestParam(defaultValue = "") String subDomain, @RequestParam(defaultValue = "") String domain) {
        if (!subDomain.isEmpty()) {
            return storeService.checkSubDomain(subDomain);
        } else if (!domain.isEmpty()) {
            return storeService.checkDomain(domain);
        }

        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/store/info")
    public ResponseEntity<StoreModel> getStoreInfoBySubDomain(@RequestParam String subDomain) {
        return storeService.getStoreInfoBySubDomain(subDomain);
    }

    @PostMapping("/store/{storeId}/image/")
    public ResponseEntity<ApiResponse<List<String>>> uploadStoreImage(@PathVariable String storeId,
                                                                      @RequestParam(value = "image", required = true) MultipartFile[] aFile) {
        return storeImageService.uploadStoreImages(storeId, aFile);
    }

    @DeleteMapping("/store/{storeId}/image/")
    public ResponseEntity<ApiResponse<List<String>>> deleteStoreImage(@PathVariable String storeId, @RequestBody List<String> imageUrls) {
        return storeImageService.deleteStoreImage(storeId, imageUrls);
    }

}
