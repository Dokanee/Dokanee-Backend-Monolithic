package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.ApiResponse;
import com.dokanne.DokaneeBackend.dto.request.TemplateRequest;
import com.dokanne.DokaneeBackend.dto.response.IdResponse;
import com.dokanne.DokaneeBackend.model.TemplateModel;
import com.dokanne.DokaneeBackend.service.TemplateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard/store/{storeId}/template")
public class TemplateController {
    private final TemplateService templateService;

    @PostMapping
    public String addTemplate(@PathVariable String storeId) {
        return templateService.createTemplate(storeId);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<TemplateModel>> getTemplateInfo(@PathVariable String storeId) {
        return templateService.getTemplateInfo(storeId);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<IdResponse>> updateTemplateInfo(@PathVariable String storeId, @RequestBody TemplateRequest templateRequest) {
        return templateService.editTemplate(storeId, templateRequest);
    }

    @PostMapping("/slider/")
    public ResponseEntity<ApiResponse<List<String>>> uploadSliderImage(@PathVariable String storeId,
                                                                       @RequestParam(value = "image", required = true) MultipartFile[] aFile) {
        return templateService.uploadSliderImage(storeId, aFile);
    }

    @DeleteMapping("/slider/")
    public ResponseEntity<ApiResponse<List<String>>> deleteSliderImage(@PathVariable String storeId, @RequestBody List<String> imageUrls) {
        return templateService.deleteSliderImage(storeId, imageUrls);
    }
}
