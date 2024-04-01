package com.VigiDrive.service;

import com.VigiDrive.config.CustomGoogleAuthenticationToken;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.response.LoginResponse;

public interface AuthService {

    LoginResponse getToken(String code) throws UserException;

    CustomGoogleAuthenticationToken getGoogleToken(String code) throws UserException;
}
