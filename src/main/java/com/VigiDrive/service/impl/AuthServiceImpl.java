package com.VigiDrive.service.impl;

import com.VigiDrive.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${spring.security.oauth2.authorizationserver.client.oidc-client.registration.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.authorizationserver.client.oidc-client.registration.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.authorizationserver.client.oidc-client.registration.redirect-uris}")
    private String redirectUri;

    @Value("${spring.security.oauth2.authorizationserver.client.oidc-client.registration.authorization-grant-types}")
    private String grantType;

    @Override
    public OAuth2AccessToken getToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        String plainCreds = clientId + ":" + clientSecret;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);

        String uri = "http://127.0.0.1:8080/oauth2/token?code=" + code +
                "&grant_type=" + grantType +
                "&redirect_uri=" + redirectUri;


        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<OAuth2AccessTokenResponse> response = restTemplate.postForEntity(uri, entity,
                OAuth2AccessTokenResponse.class);
        System.out.println("------------> token=" + response.getBody().getAccessToken());

        return response.getBody().getAccessToken();
    }
}
