package com.VigiDrive.controller;

import com.VigiDrive.config.CustomGoogleAuthenticationToken;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.response.LoginResponse;
import com.VigiDrive.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private AuthService authService;

    @GetMapping("/auth")
    public LoginResponse getAuthToken(@RequestParam("code") String code) throws UserException {
        return authService.getToken(code);
    }

//    @GetMapping("/auth/google")
//    public CustomGoogleAuthenticationToken getGoogleAuthToken(@RequestParam("code") String code) throws UserException {
//        System.out.println("Code ------------> " + code);
//        return authService.getGoogleToken(code);
//    }

    @GetMapping("/auth/google")
    public String getGoogleAuthToken(CustomGoogleAuthenticationToken token, Authentication auth) {
//        return "Token exists: " + (token != null)
//                + "\n access token=" + token.getAccessToken()
//                + "\n token type=" + token.getTokenType()
//                + "\n expires in=" + token.getExpiresIn()
//                + "\n id token=" + token.getIdToken()
//                + "\n scope=" + token.getScope();

        return "Token exists:" + (token != null) + " \nAuth exists:" + (auth != null);
    }
}
