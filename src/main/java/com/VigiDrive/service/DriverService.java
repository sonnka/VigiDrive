package com.VigiDrive.service;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface DriverService {

    DriverDTO registerDriver(RegisterRequest newDriver);

    void delete(Long driverId);

    FullDriverDTO getFullDriver(Long driverId);

    ManagerDTO getDriverManager(Long driverId);

    void updateCurrentLocation(Long driverId, String currentLocation);

    void addEmergencyNumber(Long driverId, String emergencyNumber);

    DriverDTO updateDriver(Long driverId, UpdateDriverRequest driver);

    List<ShortDriverDTO> getAllDrivers();

    List<ShortDriverDTO> getAllDriversByManager(Long managerId);
}
