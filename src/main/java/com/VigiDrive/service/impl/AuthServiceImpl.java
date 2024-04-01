package com.VigiDrive.service.impl;

import com.VigiDrive.config.CustomAuthenticationToken;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.response.LoginResponse;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    private UserRepository userRepository;

    private AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @CrossOrigin
    public LoginResponse getToken(String code) throws UserException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        String plainCreds = clientId + ":" + clientSecret;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);

        String uri = "http://localhost:8080/oauth2/token?code=" + code +
                "&grant_type=" + grantType +
                "&redirect_uri=" + redirectUri;

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomAuthenticationToken> response = restTemplate.postForEntity(uri, entity,
                CustomAuthenticationToken.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        var user = userRepository.findByEmailIgnoreCase(currentPrincipalName).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.USER_NOT_FOUND)
        );

        return LoginResponse.builder()
                .id(user.getId())
                .token(response.getBody())
                .role(user.getRole().name())
                .build();
    }

}
