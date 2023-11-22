package com.VigiDrive.controller;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.service.ManagerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class ManagerController {

    private ManagerService managerService;

    @PostMapping("/register/manager")
    public ResponseEntity<ManagerDTO> register(@Valid @RequestBody RegisterRequest newManager) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(managerService.registerManager(newManager));
    }
}
