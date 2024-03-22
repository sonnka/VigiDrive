package com.VigiDrive.service;

import com.VigiDrive.config.CustomOAuth2User;

public interface UserService {

    void processGoogleOAuthPostLogin(CustomOAuth2User oAuth2User);
}
