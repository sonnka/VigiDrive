package com.VigiDrive.service.impl;

import com.VigiDrive.config.KeycloakConfig;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final RestTemplate restTemplate;

    private final Keycloak keycloak;
    private KeycloakConfig keycloakConfig;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.serverUrl}")
    private String keycloakUrl;


    public AccessTokenResponse authenticate(AuthRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username", request.getUsername());
        parameters.add("password", request.getPassword());
        parameters.add("grant_type", "password");
        parameters.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);
        try {
            return restTemplate.exchange(getAuthUrl(),
                    HttpMethod.POST,
                    entity,
                    AccessTokenResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Can't authenticate user " + request.getUsername(), e);
        }
    }

    public AccessTokenResponse refreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "refresh_token");
        parameters.add("client_id", clientId);
        parameters.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

        return restTemplate.exchange(getAuthUrl(),
                HttpMethod.POST,
                entity,
                AccessTokenResponse.class).getBody();
    }


    public String createUser(RegisterRequest newUser, Role role) {
        UserRepresentation userRepresentation = getUserRepresentation(newUser);

        Response response = addUser(userRepresentation);

        String createdUserKeycloakId = CreatedResponseUtil.getCreatedId(response);

        addRoleToUser(role, createdUserKeycloakId);

        return createdUserKeycloakId;
    }

    private Response addUser(UserRepresentation userRepresentation) {
        UsersResource instance = getInstance();
        try (Response response = instance.create(userRepresentation)) {
            if (HttpStatus.CREATED.value() == response.getStatus()) {
                return response;
            }
            throw new RuntimeException("Something went wrong while register user in keycloak. Response Status : "
                    + response.getStatusInfo().getReasonPhrase());
        }
    }

    public void deleteUser(String userId) {
        UsersResource instance = getInstance();
        String reasonPhrase = null;
        try (Response response = instance.delete(userId)) {
            reasonPhrase = response.getStatusInfo().getReasonPhrase();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong while deleting user in keycloak. Response Status : "
                    + reasonPhrase);
        }
    }

    public void updateUser(UserRepresentation representation) {
        UsersResource instance = getInstance();
        instance.get(representation.getId()).update(representation);
    }

    private UsersResource getInstance() {
        return keycloak.realm(realm).users();
    }

    private String getAuthUrl() {
        return UriComponentsBuilder.fromHttpUrl(keycloakUrl)
                .pathSegment("realms")
                .pathSegment(realm)
                .pathSegment("protocol")
                .pathSegment("openid-connect")
                .pathSegment("token")
                .toUriString();
    }


    private UserRepresentation getUserRepresentation(RegisterRequest newUser) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(newUser.getFirstName());
        userRepresentation.setLastName(newUser.getLastName());
        userRepresentation.setEmail(newUser.getEmail());
        userRepresentation.setUsername(newUser.getEmail());
        userRepresentation.setEmailVerified(Boolean.TRUE);
        userRepresentation.setEnabled(Boolean.TRUE);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(newUser.getPassword());
        credentialRepresentation.setTemporary(Boolean.FALSE);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }

    private void addRoleToUser(Role role, String keycloakId) {
        RealmResource realmResource = keycloak.realm(realm);
        RoleRepresentation roleRepresentation = realmResource
                .roles()
                .get(role.name())
                .toRepresentation();
        RoleScopeResource roleScopeResource = realmResource.users()
                .get(keycloakId)
                .roles()
                .realmLevel();
        if (roleScopeResource.listAll().size() > 1) {
            roleScopeResource.remove(List.of(roleScopeResource.listAll().get(1)));
        }
        roleScopeResource.add(List.of(roleRepresentation));
    }
}