package com.VigiDrive.controller;

import com.VigiDrive.model.response.LoginResponse;
import com.VigiDrive.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(Authentication authentication) {
        return authService.login(authentication);
    }

//    @PostMapping("/api/sensordata")
//    public String receiveSensorData(@RequestBody String sensorData) {
//        log.error("Received sensor data: " + sensorData);
//
//        return "Received sensor data successfully";
//    }
}
