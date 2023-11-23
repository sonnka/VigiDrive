package com.VigiDrive.service.impl;

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
    public ManagerDTO registerManager(RegisterRequest newManager) {
        String keycloakId = keycloakService.createUser(newManager, Role.MANAGER);

        if (keycloakId == null || keycloakId.isEmpty()) {
            throw new RuntimeException("Registration failed.");
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
    public List<ShortDriverDTO> getDrivers(Long managerId) {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        return driverService.getAllDriversByManager(manager.getId());
    }

    @Override
    public FullDriverDTO getDriver(Long managerId, Long driverId) {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        var driver = driverService.getFullDriver(driverId);

        if (!Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new RuntimeException("Permission denied");
        }

        return driver;
    }

    @Override
    public ManagerDTO updateManager(Long managerId, UpdateManagerRequest newManager) {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        manager.setFirstName(newManager.getFirstName());
        manager.setLastName(newManager.getLastName());
        manager.setAvatar(newManager.getAvatar());

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    public FullManagerDTO getManager(Long managerId) {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        return new FullManagerDTO(manager);
    }

    @Override
    public void setDestinationForDriver(Long managerId, Long driverId, String destination) {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        if (!Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new RuntimeException("Permission denied");
        }

        driver.setDestination(destination);

        driverRepository.save(driver);
    }

    @Override
    public void delete(Long managerId) {
        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found!"));

        keycloakService.deleteUser(manager.getKeycloakId().toString());

        managerRepository.delete(manager);
    }

    @Override
    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll().stream().map(ManagerDTO::new).toList();
    }
}
