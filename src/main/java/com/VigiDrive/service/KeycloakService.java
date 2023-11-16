package com.VigiDrive.service;

import com.VigiDrive.model.request.AuthRequestDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {
    AccessTokenResponse authenticate(AuthRequestDto request);

    AccessTokenResponse authenticateGoogle();

}
