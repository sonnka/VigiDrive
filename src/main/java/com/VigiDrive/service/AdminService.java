package com.VigiDrive.service;

import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface AdminService {

    AdminDTO registerAdmin(RegisterRequest newAdmin);

    void deleteDriver(Long adminId, Long driverId);

    void deleteManager(Long adminId, Long managerId);

    List<ShortDriverDTO> getDrivers(Long adminId);

    List<ManagerDTO> getManagers(Long adminId);
}
