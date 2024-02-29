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

    void deleteDriver(String email, Long adminId, Long driverId) throws UserException, SecurityException;

    void deleteManager(String email, Long adminId, Long managerId) throws UserException, SecurityException;

    List<ShortDriverDTO> getDrivers(String email, Long adminId) throws UserException;

    List<ManagerDTO> getManagers(String email, Long adminId) throws UserException;
}
