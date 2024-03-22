package com.VigiDrive.controller;

import com.VigiDrive.model.response.LoginResponse;
import com.VigiDrive.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private AuthService authService;

//    @PostMapping("/login")
//    public LoginResponse login(Authentication authentication) {
//        return authService.login(authentication);
//    }

    @GetMapping("/login/google")
    public LoginResponse loginGoogle(@RequestParam String email) {
        return authService.login(email);
    }
}
