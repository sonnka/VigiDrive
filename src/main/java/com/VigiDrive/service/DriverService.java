package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DriverService {

    DriverDTO registerDriver(RegisterRequest newDriver) throws SecurityException;

    void delete(Authentication auth, Long driverId) throws UserException, SecurityException;

    FullDriverDTO getFullDriver(Authentication auth, Long driverId) throws UserException;

    ManagerDTO getDriverManager(Authentication auth, Long driverId) throws UserException;

    void updateCurrentLocation(Authentication auth, Long driverId, String currentLocation) throws UserException;

    void addEmergencyNumber(Authentication auth, Long driverId, String emergencyNumber) throws UserException;

    DriverDTO updateDriver(Authentication auth, Long driverId, UpdateDriverRequest driver) throws UserException;

    List<ShortDriverDTO> getAllDrivers(Authentication auth);

    List<ShortDriverDTO> getAllDriversByManager(Authentication auth, Long managerId);
}
