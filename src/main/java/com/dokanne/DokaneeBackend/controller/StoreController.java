package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/store")
    public ResponseEntity<String> createStore(@RequestBody StoreRequest storeRequest) {
        return storeService.createStore(storeRequest);
    }

    @GetMapping("/store")
    public ResponseEntity<Object> getStoreInfo() {
        return storeService.getStoreInfo();
    }

    @PutMapping("/store/{storeId}")
    public ResponseEntity editStoreInfo(@PathVariable String storeId, @RequestBody StoreRequest storeRequest) {
        return storeService.editStore(storeId, storeRequest);
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


}
