package com.VigiDrive.controller;

import com.VigiDrive.model.request.AuthRequestDto;
import com.VigiDrive.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private KeycloakService keycloakService;
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody AuthRequestDto request){
        return ResponseEntity.ok(keycloakService.authenticate(request));
    }

    @GetMapping("/login/google")
    public ResponseEntity<AccessTokenResponse> loginGoogle(){
        return ResponseEntity.ok(keycloakService.authenticateGoogle());
    }

    @GetMapping("/hello")
    public String hello(Authentication auth){
        return "Hello ";
    }
}
