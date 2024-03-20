package com.VigiDrive.util;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.entity.User;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthUtil {

    private AdminRepository adminRepository;
    private DriverRepository driverRepository;
    private ManagerRepository managerRepository;
    private UserRepository userRepository;

    public User findUserByEmailAndId(String email, Long userId) throws UserException {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!user.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return user;
    }

    public Driver findDriverByEmailAndId(String email, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!driver.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return driver;
    }

    public Driver findDriverByEmailAndIdAndCheckByAdmin(String email, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!driver.getEmail().equals(email) && (!adminRepository.existsByEmailIgnoreCase(email))) {
            throw new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND);
        }

        return driver;
    }

    public Driver findDriverByEmailAndIdAndCheckByManager(String email, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!driver.getEmail().equals(email)) {
            var manager = managerRepository.findByEmailIgnoreCase(email).orElseThrow(
                    () -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

            if (!manager.getDrivers().contains(driver)) {
                throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
            }
        }
        return driver;
    }

    public Manager findManagerByEmailAndId(String email, Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (!manager.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return manager;
    }

    public Manager findManagerByEmailAndIdAndCheckByAdmin(String email, Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (!manager.getEmail().equals(email) && (!adminRepository.existsByEmailIgnoreCase(email))) {
            throw new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND);
        }

        return manager;
    }

    public Admin checkAdminByEmailAndId(String email, Long adminId) throws UserException {
        var admin = adminRepository.findById(adminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return admin;
    }

    public void checkAdminByEmailAndChief(String email, Long adminId) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.getId().equals(adminId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        if (!Role.CHIEF_ADMIN.equals(admin.getRole())) {
            throw new UserException(UserException.UserExceptionProfile.NOT_CHIEF_ADMIN);
        }
    }
}
