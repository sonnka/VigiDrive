package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.model.request.RegisterRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {
    AccessTokenResponse authenticate(AuthRequest request) throws SecurityException;

    AccessTokenResponse refreshToken(String refreshToken);

    String createUser(RegisterRequest newUser, Role role) throws SecurityException;

    void deleteUser(String userId) throws SecurityException;

    void updateUser(UserRepresentation representation);
}
