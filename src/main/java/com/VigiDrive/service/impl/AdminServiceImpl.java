package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.service.AdminService;
import com.VigiDrive.service.DriverService;
import com.VigiDrive.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private DriverService driverService;
    private ManagerService managerService;
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO registerAdmin(RegisterRequest newAdmin) {

        Admin admin = Admin.builder()
                .email(newAdmin.getEmail())
                .firstName(newAdmin.getFirstName())
                .lastName(newAdmin.getLastName())
                .password(passwordEncoder.encode(newAdmin.getPassword()))
                .role(Role.ADMIN)
                .isApproved(false)
                .build();

        return new AdminDTO(adminRepository.save(admin));
    }

    @Override
    public void deleteDriver(Authentication auth, Long adminId, Long driverId)
            throws UserException, SecurityException {
        adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        driverService.delete(auth, driverId);
    }

    @Override
    public void deleteManager(Authentication auth, Long adminId, Long managerId)
            throws UserException, SecurityException {
        adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        managerService.delete(auth, managerId);
    }

    @Override
    public List<ShortDriverDTO> getDrivers(Authentication auth, Long adminId) throws UserException {
        adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        return driverService.getAllDrivers(auth);
    }

    @Override
    public List<ManagerDTO> getManagers(Authentication auth, Long adminId) throws UserException {
        adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        return managerService.getAllManagers(auth);
    }
}
