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
import com.VigiDrive.service.KeycloakService;
import com.VigiDrive.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private DriverService driverService;
    private ManagerService managerService;
    private KeycloakService keycloakService;
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO registerAdmin(RegisterRequest newAdmin) throws SecurityException {
        String keycloakId = keycloakService.createUser(newAdmin, Role.ADMIN);

        if (keycloakId == null || keycloakId.isEmpty()) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.REGISTRATION_FAILED);
        }

        Admin admin = Admin.builder()
                .email(newAdmin.getEmail())
                .firstName(newAdmin.getFirstName())
                .lastName(newAdmin.getLastName())
                .keycloakId(UUID.fromString(keycloakId))
                .password(passwordEncoder.encode(newAdmin.getPassword()))
                .role(Role.ADMIN)
                .isApproved(false)
                .build();

        return new AdminDTO(adminRepository.save(admin));
    }

    @Override
    public void deleteDriver(Long adminId, Long driverId) throws UserException, SecurityException {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        driverService.delete(driverId);
    }

    @Override
    public void deleteManager(Long adminId, Long managerId) throws UserException, SecurityException {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        managerService.delete(managerId);
    }

    @Override
    public List<ShortDriverDTO> getDrivers(Long adminId) throws UserException {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        return driverService.getAllDrivers();
    }

    @Override
    public List<ManagerDTO> getManagers(Long adminId) throws UserException {
        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        return managerService.getAllManagers();
    }
}
