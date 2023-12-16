package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ManagerService {
    ManagerDTO registerManager(RegisterRequest newManager) throws SecurityException;

    List<ShortDriverDTO> getDrivers(Authentication auth, Long managerId) throws UserException;

    FullDriverDTO getDriver(Authentication auth, Long managerId, Long driverId) throws UserException;

    ManagerDTO updateManager(Authentication auth, Long managerId, UpdateManagerRequest manager) throws UserException;

    FullManagerDTO getManager(Authentication auth, Long managerId) throws UserException;

    void setDestinationForDriver(Authentication auth, Long managerId, Long driverId, String destination)
            throws UserException;

    void delete(Authentication auth, Long managerId) throws UserException, SecurityException;

    List<ManagerDTO> getAllManagers(Authentication auth);
}
