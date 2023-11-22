package com.VigiDrive.service;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface ManagerService {
    ManagerDTO registerManager(RegisterRequest newManager);

    List<ShortDriverDTO> getDrivers(Long managerId);

    FullDriverDTO getDriver(Long managerId, Long driverId);
}
