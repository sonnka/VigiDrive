package com.VigiDrive.service;

import com.VigiDrive.config.CustomAuthenticationToken;

public interface AuthService {

    CustomAuthenticationToken getToken(String code);
}
