package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.StoreRequest;
import com.dokanne.DokaneeBackend.service.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/create")
    public String createStore(@RequestBody StoreRequest storeRequest) {
        return storeService.createStore(storeRequest);
    }
}
