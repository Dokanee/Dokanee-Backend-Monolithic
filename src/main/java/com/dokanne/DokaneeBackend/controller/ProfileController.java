package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.dto.request.ProfileRequest;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/")
    public ResponseEntity editUserInfo(@RequestBody ProfileRequest profileRequest) {
        return profileService.editUserInfo(profileRequest);
    }

}
