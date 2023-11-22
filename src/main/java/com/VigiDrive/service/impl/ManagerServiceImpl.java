package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.service.KeycloakService;
import com.VigiDrive.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepository;
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
        return null;
    }

    @Override
    public FullDriverDTO getDriver(Long managerId, Long driverId) {
        return null;
    }
}
