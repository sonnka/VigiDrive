package com.VigiDrive.controller;

import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private KeycloakService keycloakService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(keycloakService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@RequestHeader("refresh-token") String refreshToken) {
        return ResponseEntity.ok(keycloakService.refreshToken(refreshToken));
    }

    @GetMapping("/hello")
    public String hello(Authentication auth) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello \n" + auth.getName() + "\n---------\n" + authentication.getName() + "\n---------\n";
    }
}
