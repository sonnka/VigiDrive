package com.VigiDrive.controller;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private KeycloakService keycloakService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody AuthRequest request) throws SecurityException {
        return ResponseEntity.ok(keycloakService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@RequestHeader("refresh-token") String refreshToken) {
        return ResponseEntity.ok(keycloakService.refreshToken(refreshToken));
    }
}
