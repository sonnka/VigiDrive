package com.VigiDrive.controller;

import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) throws SecurityException {
        return authService.loginUser(request);
    }
}
