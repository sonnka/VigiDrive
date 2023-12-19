package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdminService {

    AdminDTO registerAdmin(RegisterRequest newAdmin) throws SecurityException;

    void deleteDriver(Authentication auth, Long adminId, Long driverId) throws UserException, SecurityException;

    void deleteManager(Authentication auth, Long adminId, Long managerId) throws UserException, SecurityException;

    List<ShortDriverDTO> getDrivers(Authentication auth, Long adminId) throws UserException;

    List<ManagerDTO> getManagers(Authentication auth, Long adminId) throws UserException;
}
