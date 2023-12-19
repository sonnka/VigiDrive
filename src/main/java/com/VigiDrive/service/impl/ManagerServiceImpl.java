package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.service.DriverService;
import com.VigiDrive.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepository;
    private DriverService driverService;
    private DriverRepository driverRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public ManagerDTO registerManager(RegisterRequest newManager) {

        Manager manager = Manager.builder()
                .email(newManager.getEmail())
                .firstName(newManager.getFirstName())
                .lastName(newManager.getLastName())
                .password(passwordEncoder.encode(newManager.getPassword()))
                .role(Role.MANAGER)
                .build();

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    public List<ShortDriverDTO> getDrivers(Authentication auth, Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        return driverService.getAllDriversByManager(auth, manager.getId());
    }

    @Override
    public FullDriverDTO getDriver(Authentication auth, Long managerId, Long driverId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getManager() == null || !Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        return driverService.getFullDriver(auth, driverId);
    }

    @Override
    public ManagerDTO updateManager(Authentication auth, Long managerId, UpdateManagerRequest newManager)
            throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        manager.setFirstName(newManager.getFirstName());
        manager.setLastName(newManager.getLastName());
        manager.setAvatar(newManager.getAvatar());

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    public FullManagerDTO getManager(Authentication auth, Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        return new FullManagerDTO(manager);
    }

    @Override
    public void setDestinationForDriver(Authentication auth, Long managerId, Long driverId, String destination)
            throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getManager() == null || !Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        driver.setDestination(destination);

        driverRepository.save(driver);
    }

    @Override
    public void delete(Authentication auth, Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        managerRepository.delete(manager);
    }

    @Override
    public List<ManagerDTO> getAllManagers(Authentication auth) {
        return managerRepository.findAll().stream().map(ManagerDTO::new).toList();
    }
}
