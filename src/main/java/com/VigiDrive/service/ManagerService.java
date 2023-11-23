package com.VigiDrive.service;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface ManagerService {
    ManagerDTO registerManager(RegisterRequest newManager);

    List<ShortDriverDTO> getDrivers(Long managerId);

    FullDriverDTO getDriver(Long managerId, Long driverId);

    ManagerDTO updateManager(Long managerId, UpdateManagerRequest manager);

    FullManagerDTO getManager(Long managerId);

    void setDestinationForDriver(Long managerId, Long driverId, String destination);

    void delete(Long managerId);

    List<ManagerDTO> getAllManagers();
}
