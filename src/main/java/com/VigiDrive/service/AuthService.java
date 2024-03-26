package com.VigiDrive.service;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

public interface AuthService {

    OAuth2AccessToken getToken(String code);
}
