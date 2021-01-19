package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.request.ProfileRequest;
import com.dokanne.DokaneeBackend.jwt.dto.request.PassChangeRequest;
import com.dokanne.DokaneeBackend.jwt.services.SignUpAndSignInService;
import com.dokanne.DokaneeBackend.repository.ProfileRepository;
import com.dokanne.DokaneeBackend.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final SignUpAndSignInService signUpAndSignInService;

    @GetMapping("/")
    public ResponseEntity getUserInfo() {
        return profileService.getUserProfile();
    }

    @PutMapping("/")
    public ResponseEntity editUserInfo(@RequestBody ProfileRequest profileRequest) {
        return profileService.editUserInfo(profileRequest);
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity postImage(@RequestParam(value = "image", required = true) MultipartFile aFile) {
        return profileService.uploadImage(aFile);
    }

    @PostMapping("/changePass")
    public ResponseEntity changePass(@RequestBody PassChangeRequest passChangeRequest) {
        return signUpAndSignInService.changePass(passChangeRequest);
    }

}
