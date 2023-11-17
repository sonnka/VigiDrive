package com.VigiDrive.service;

import com.VigiDrive.model.request.AuthRequest;
import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakService {
    AccessTokenResponse authenticate(AuthRequest request);

    AccessTokenResponse authenticateGoogle();

}
