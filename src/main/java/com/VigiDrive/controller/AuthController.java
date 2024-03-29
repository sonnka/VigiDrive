package com.VigiDrive.controller;

import com.VigiDrive.config.CustomAuthenticationToken;
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

//
//    @GetMapping("/auth")
//    public String getCode(@RequestParam("code") String code) {
//        return code;
//    }

    @GetMapping("/auth")
    public CustomAuthenticationToken getAuthToken(@RequestParam("code") String code) {
        System.out.println("-------------> code=" + code);
        return authService.getToken(code);
    }
}
