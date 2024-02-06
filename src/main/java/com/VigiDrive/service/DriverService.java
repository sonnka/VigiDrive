package com.VigiDrive.service;

import com.VigiDrive.exceptions.AmazonException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverService {

    DriverDTO registerDriver(RegisterRequest newDriver) throws SecurityException;

    DriverDTO updateDriver(String email, Long driverId, UpdateDriverRequest driver) throws UserException;

    DriverDTO uploadAvatar(String email, Long driverId, MultipartFile avatar) throws UserException, AmazonException;

    void delete(String email, Long driverId) throws UserException, SecurityException;

    FullDriverDTO getFullDriver(String email, Long driverId) throws UserException;

    ManagerDTO getDriverManager(String email, Long driverId) throws UserException;

    void updateCurrentLocation(String email, Long driverId, String currentLocation) throws UserException;

    void addEmergencyNumber(String email, Long driverId, String emergencyNumber) throws UserException;

    List<ShortDriverDTO> getAllDriversByManager(String email, Long managerId);
}
