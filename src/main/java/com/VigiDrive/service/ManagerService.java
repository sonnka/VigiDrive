package com.VigiDrive.service;

import com.VigiDrive.exceptions.AmazonException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ManagerService {

    ManagerDTO registerManager(RegisterRequest newManager) throws SecurityException;

    ManagerDTO updateManager(String email, Long managerId, UpdateManagerRequest manager) throws UserException;

    ManagerDTO uploadAvatar(String email, Long managerId, MultipartFile avatar) throws UserException, AmazonException;

    void delete(String email, Long managerId) throws UserException, SecurityException;

    void delete(Manager manager);

    List<ShortDriverDTO> getDrivers(String email, Long managerId) throws UserException;

    FullDriverDTO getDriver(String email, Long managerId, Long driverId) throws UserException;

    FullManagerDTO getManager(String email, Long managerId) throws UserException;

    void setDestinationForDriver(String email, Long managerId, Long driverId, String destination)
            throws UserException;
}
