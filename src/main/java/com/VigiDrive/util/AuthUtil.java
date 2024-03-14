package com.VigiDrive.util;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;

public class AuthUtil {

    private AuthUtil() {

    }

    public static Driver findDriverByEmailAndId(String email, Long driverId, DriverRepository driverRepository)
            throws UserException {
        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!driver.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        if (!Role.DRIVER.equals(driver.getRole())) {
            throw new UserException(UserException.UserExceptionProfile.NOT_DRIVER);
        }
        return driver;
    }

    public static Driver findDriverByEmailAndIdAndCheckByAdmin(String email, Long driverId,
                                                               DriverRepository driverRepository,
                                                               AdminRepository adminRepository)
            throws UserException {
        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!driver.getEmail().equals(email)) {
            var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                    () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

            if (!Role.ADMIN.equals(admin.getRole())) {
                throw new UserException(UserException.UserExceptionProfile.NOT_ADMIN);
            }
        }

        return driver;
    }

    public static Driver findDriverByEmailAndIdAndCheckByManager(String email, Long driverId,
                                                                 DriverRepository driverRepository,
                                                                 ManagerRepository managerRepository)
            throws UserException {
        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!driver.getEmail().equals(email)) {
            var manager = managerRepository.findByEmailIgnoreCase(email).orElseThrow(
                    () -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

            if (!Role.MANAGER.equals(manager.getRole())) {
                throw new UserException(UserException.UserExceptionProfile.NOT_MANAGER);
            }

            if (!manager.getDrivers().contains(driver)) {
                throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
            }
        }
        return driver;
    }

    public static Manager findManagerByEmailAndId(String email, Long managerId, ManagerRepository managerRepository)
            throws UserException {
        var manager = managerRepository.findById(managerId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (!manager.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        if (!Role.MANAGER.equals(manager.getRole())) {
            throw new UserException(UserException.UserExceptionProfile.NOT_MANAGER);
        }
        return manager;
    }

    public static Manager findManagerByEmailAndIdAndCheckByAdmin(String email, Long managerId,
                                                                 ManagerRepository managerRepository,
                                                                 AdminRepository adminRepository)
            throws UserException {
        var manager = managerRepository.findById(managerId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (!manager.getEmail().equals(email)) {
            var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                    () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

            if (!Role.ADMIN.equals(admin.getRole())) {
                throw new UserException(UserException.UserExceptionProfile.NOT_ADMIN);
            }
        }

        return manager;
    }
}
