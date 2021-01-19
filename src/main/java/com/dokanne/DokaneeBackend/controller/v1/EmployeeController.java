package com.dokanne.DokaneeBackend.controller.v1;

import com.dokanne.DokaneeBackend.dto.request.NewEmployeeRequest;
import com.dokanne.DokaneeBackend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RestController
@RequestMapping("/dashboard/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/")
    public ResponseEntity getEmployeeList(@RequestParam String storeId) {
        return employeeService.getEmployeeList(storeId);

    }

    @GetMapping("/checkEmail")
    public ResponseEntity getEmailAvailable(@RequestParam String email) {
        return employeeService.getEmailAvailable(email);
    }

    @PostMapping("/addEmployee")
    public ResponseEntity addEmployeeViaEmail(@RequestParam String email, @RequestParam String storeId) throws IOException, MessagingException {
        return employeeService.addEmployeeViaEmail(email, storeId);
    }

    @PostMapping("/addNewEmployee")
    public ResponseEntity addNewEmployee(@RequestBody NewEmployeeRequest newEmployeeRequest, @RequestParam String storeId) throws IOException, MessagingException {
        return employeeService.addNewEmployee(newEmployeeRequest, storeId);
    }

}
