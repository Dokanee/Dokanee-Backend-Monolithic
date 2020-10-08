package com.dokanne.DokaneeBackend.controller;

import com.dokanne.DokaneeBackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/")
    public ResponseEntity getEmployeeList(@RequestParam String storeId){
        return employeeService.getEmployeeList(storeId);

    }

}
