package com.VigiDrive.service.impl;

import com.VigiDrive.model.request.AuthRequestDto;
import com.VigiDrive.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.serverUrl}")
    private String keycloakUrl;

    private final RestTemplate restTemplate;

    private final Keycloak keycloak;

    public AccessTokenResponse authenticate(AuthRequestDto request) {
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

    public AccessTokenResponse authenticateGoogle() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//        parameters.add("username", request.getUsername());
//        parameters.add("password", request.getPassword());
//        parameters.add("grant_type", "password");
//        parameters.add("client_id", clientId);
//
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);
        try {
            return restTemplate.exchange(getAuthUrl(),
                    HttpMethod.GET,
                    null,
                    AccessTokenResponse.class).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Can't authenticate user " , e);
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

    public Response addUser(UserRepresentation userRepresentation) {
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
            throw new RuntimeException("Something went wrong while register user in keycloak. Response Status : "
                    + reasonPhrase);
        }
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

    private String getGoogleAuthUrl() {
        //http://localhost:8085/realms/myapp/broker/google/endpoint
        return UriComponentsBuilder.fromHttpUrl(keycloakUrl)
                .pathSegment("realms")
                .pathSegment(realm)
                .pathSegment("broker")
                .pathSegment("google")
                .pathSegment("endpoint")
                .toUriString();
    }

    private UsersResource getInstance() {
        return keycloak.realm(realm).users();
    }

    public void updateUser(UserRepresentation representation) {
        UsersResource instance = getInstance();
        instance.get(representation.getId()).update(representation);
    }
}
