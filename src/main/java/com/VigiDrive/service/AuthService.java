package com.VigiDrive.service;

import com.VigiDrive.model.response.LoginResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    LoginResponse login(Authentication auth);
}
