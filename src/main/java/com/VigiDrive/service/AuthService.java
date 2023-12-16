package com.VigiDrive.service;

import com.VigiDrive.model.request.AuthRequest;

public interface AuthService {

    String loginUser(AuthRequest user);
}
