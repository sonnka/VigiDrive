package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface DriverService {

    DriverDTO registerDriver(RegisterRequest newDriver) throws SecurityException;

    void delete(Long driverId) throws UserException, SecurityException;

    FullDriverDTO getFullDriver(Long driverId) throws UserException;

    ManagerDTO getDriverManager(Long driverId) throws UserException;

    void updateCurrentLocation(Long driverId, String currentLocation) throws UserException;

    void addEmergencyNumber(Long driverId, String emergencyNumber) throws UserException;

    DriverDTO updateDriver(Long driverId, UpdateDriverRequest driver) throws UserException;

    List<ShortDriverDTO> getAllDrivers();

    List<ShortDriverDTO> getAllDriversByManager(Long managerId);
}
