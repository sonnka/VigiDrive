package com.VigiDrive.controller;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.service.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping("/register/admin")
    public ResponseEntity<AdminDTO> register(@Valid @RequestBody RegisterRequest newAdmin) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.registerAdmin(newAdmin));
    }
}
