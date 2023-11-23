package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface ManagerService {
    ManagerDTO registerManager(RegisterRequest newManager) throws SecurityException;

    List<ShortDriverDTO> getDrivers(Long managerId) throws UserException;

    FullDriverDTO getDriver(Long managerId, Long driverId) throws UserException;

    ManagerDTO updateManager(Long managerId, UpdateManagerRequest manager) throws UserException;

    FullManagerDTO getManager(Long managerId) throws UserException;

    void setDestinationForDriver(Long managerId, Long driverId, String destination) throws UserException;

    void delete(Long managerId) throws UserException, SecurityException;

    List<ManagerDTO> getAllManagers();
}
