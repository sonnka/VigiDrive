package com.VigiDrive.service.impl;

import com.VigiDrive.model.request.AuthRequest;
import com.VigiDrive.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final String AUTH_SERVER_BASE_URL = "http://localhost:8080";
    private final String LOGIN_ENDPOINT = "/oauth/token";

    private String clientId = "oidc-client";

    private String clientSecret = "{noop}secret";

    @Autowired
    private RestTemplate restTemplate;

    public String loginUser(AuthRequest user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> loginRequest = new LinkedMultiValueMap<>();
        loginRequest.add("grant_type", "client_credentials");
        loginRequest.add("username", user.getUsername());
        loginRequest.add("password", user.getPassword());
        loginRequest.add("scope", "scope-token");
        loginRequest.add("client_id", clientId);
        loginRequest.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(loginRequest, headers);

        return restTemplate.postForObject(AUTH_SERVER_BASE_URL + LOGIN_ENDPOINT, request, String.class);
    }

}
