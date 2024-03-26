package com.VigiDrive.controller;

import com.VigiDrive.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private AuthService authService;

    @GetMapping("/")
    public OAuth2AccessToken test(@RequestParam("code") String code) {
        return authService.getToken(code);
    }

//    @GetMapping("/")
//    public String test(OAuth2AccessTokenResponse object) {
//        System.out.println("-----------> " + (object == null));
//
//        return object.getAccessToken().getTokenValue();
//    }
}
