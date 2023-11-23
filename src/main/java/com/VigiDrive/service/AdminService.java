package com.VigiDrive.service;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface AdminService {

    AdminDTO registerAdmin(RegisterRequest newAdmin) throws SecurityException;

    void deleteDriver(Long adminId, Long driverId) throws UserException, SecurityException;

    void deleteManager(Long adminId, Long managerId) throws UserException, SecurityException;

    List<ShortDriverDTO> getDrivers(Long adminId) throws UserException;

    List<ManagerDTO> getManagers(Long adminId) throws UserException;
}
