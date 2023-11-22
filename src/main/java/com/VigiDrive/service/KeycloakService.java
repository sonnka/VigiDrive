package com.VigiDrive.service;

import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.model.request.RegisterRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {
    AccessTokenResponse authenticate(AuthRequest request);

    AccessTokenResponse refreshToken(String refreshToken);

    String createUser(RegisterRequest newUser, Role role);

    void deleteUser(String userId);

    void updateUser(UserRepresentation representation);
}
