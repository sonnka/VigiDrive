package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SecurityException;
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
import com.VigiDrive.service.KeycloakService;
import com.VigiDrive.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepository;
    private DriverService driverService;
    private DriverRepository driverRepository;
    private KeycloakService keycloakService;
    private PasswordEncoder passwordEncoder;

    @Override
    public ManagerDTO registerManager(RegisterRequest newManager) throws SecurityException {
        String keycloakId = keycloakService.createUser(newManager, Role.MANAGER);

        if (keycloakId == null || keycloakId.isEmpty()) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.REGISTRATION_FAILED);
        }


        Manager manager = Manager.builder()
                .email(newManager.getEmail())
                .firstName(newManager.getFirstName())
                .lastName(newManager.getLastName())
                .keycloakId(UUID.fromString(keycloakId))
                .password(passwordEncoder.encode(newManager.getPassword()))
                .role(Role.MANAGER)
                .build();

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    public List<ShortDriverDTO> getDrivers(Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        return driverService.getAllDriversByManager(manager.getId());
    }

    @Override
    public FullDriverDTO getDriver(Long managerId, Long driverId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        var driver = driverService.getFullDriver(driverId);

        if (!Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new RuntimeException("Permission denied");
        }

        return driver;
    }

    @Override
    public ManagerDTO updateManager(Long managerId, UpdateManagerRequest newManager) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        manager.setFirstName(newManager.getFirstName());
        manager.setLastName(newManager.getLastName());
        manager.setAvatar(newManager.getAvatar());

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    public FullManagerDTO getManager(Long managerId) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        return new FullManagerDTO(manager);
    }

    @Override
    public void setDestinationForDriver(Long managerId, Long driverId, String destination) throws UserException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (!Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        driver.setDestination(destination);

        driverRepository.save(driver);
    }

    @Override
    public void delete(Long managerId) throws UserException, SecurityException {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        keycloakService.deleteUser(manager.getKeycloakId().toString());

        managerRepository.delete(manager);
    }

    @Override
    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll().stream().map(ManagerDTO::new).toList();
    }
}
