package com.dokanne.DokaneeBackend.controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "System is up and running";
    }
}
