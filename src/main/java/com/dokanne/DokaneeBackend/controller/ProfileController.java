package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity getUserInfo() {
        return profileService.getUserProfile();
    }

}
