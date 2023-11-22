package com.VigiDrive.service;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.ManagerDTO;

public interface ManagerService {
    ManagerDTO registerManager(RegisterRequest newManager);
}
