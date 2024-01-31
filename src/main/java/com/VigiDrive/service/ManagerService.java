package com.VigiDrive.service;

import com.VigiDrive.exceptions.AmazonException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManagerService {
    ManagerDTO registerManager(RegisterRequest newManager) throws SecurityException;

    ManagerDTO updateManager(Authentication auth, Long managerId, UpdateManagerRequest manager) throws UserException;

    ManagerDTO uploadAvatar(Authentication auth, Long managerId, MultipartFile avatar) throws UserException, AmazonException;

    void delete(Authentication auth, Long managerId) throws UserException, SecurityException;

    List<ShortDriverDTO> getDrivers(Authentication auth, Long managerId) throws UserException;

    FullDriverDTO getDriver(Authentication auth, Long managerId, Long driverId) throws UserException;

    FullManagerDTO getManager(Authentication auth, Long managerId) throws UserException;

    void setDestinationForDriver(Authentication auth, Long managerId, Long driverId, String destination)
            throws UserException;

    List<ManagerDTO> getAllManagers(Authentication auth);
}
