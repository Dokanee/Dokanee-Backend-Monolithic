package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.dto.response.StoreInfoResponse;
import com.dokanne.DokaneeBackend.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@RestController
@RequestMapping("/dashboard/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/create")
    public String createStore(@RequestBody StoreRequest storeRequest) {
        return storeService.createStore(storeRequest);
    }

    @GetMapping("/")
    public ResponseEntity<StoreInfoResponse> getStoreInfo() {
        return storeService.getStoreInfo();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkSubDomain(@RequestParam(defaultValue = "") String subDomain, @RequestParam(defaultValue = "") String domain) {
        if (!subDomain.isEmpty()) {
            return storeService.checkSubDomain(subDomain);
        } else if (!domain.isEmpty()) {
            return storeService.checkDomain(domain);
        }
        return new ResponseEntity(true, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/edit")
    public ResponseEntity editStoreInfo(@RequestParam String storeId, @RequestBody StoreRequest storeRequest) {
        return storeService.editStore(storeId, storeRequest);
    }

}
