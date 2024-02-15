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
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private DriverRepository driverRepository;
    private ManagerRepository managerRepository;
    private AmazonClient amazonClient;

    @Override
    public AdminDTO registerAdmin(RegisterRequest newAdmin) throws SecurityException {

        var user = userRepository.findByEmailIgnoreCase(newAdmin.getEmail()).orElse(null);

        if (user != null) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.EMAIL_OCCUPIED);
        }

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
    public void deleteDriver(String email, Long adminId, Long driverId)
            throws UserException {
        checkAdminByEmailAndId(email, adminId);

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getAvatar() != null && !driver.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(driver.getAvatar());
        }

        driverRepository.delete(driver);
    }

    @Override
    public void deleteManager(String email, Long adminId, Long managerId)
            throws UserException {
        checkAdminByEmailAndId(email, adminId);

        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (manager.getAvatar() != null && !manager.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(manager.getAvatar());
        }

        managerRepository.delete(manager);
    }

    @Override
    public List<ShortDriverDTO> getDrivers(String email, Long adminId) throws UserException {
        checkAdminByEmailAndId(email, adminId);

        return driverRepository.findAll().stream().map(ShortDriverDTO::new).toList();
    }

    @Override
    public List<ManagerDTO> getManagers(String email, Long adminId) throws UserException {
        checkAdminByEmailAndId(email, adminId);

        return managerRepository.findAll().stream().map(ManagerDTO::new).toList();
    }

    private void checkAdminByEmailAndId(String email, Long adminId) throws UserException {
        var admin = adminRepository.findById(adminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        if (!Role.ADMIN.equals(admin.getRole())) {
            throw new UserException(UserException.UserExceptionProfile.NOT_ADMIN);
        }
    }
}
