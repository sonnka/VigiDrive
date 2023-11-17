package com.VigiDrive.service.impl;

import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final RestTemplate restTemplate;
    private final Keycloak keycloak;
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
            throw new RuntimeException("Can't authenticate user ", e);
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
        //https://accounts.google.com/o/oauth2/auth
        //GET /realms/{realm_name}/protocol/openid-connect/auth
        return UriComponentsBuilder.fromHttpUrl(keycloakUrl)
                .pathSegment("realms")
                .pathSegment(realm)
                .pathSegment("broker")
                .pathSegment("google")
                .pathSegment("endpoint")
                .toUriString();
    }
}
