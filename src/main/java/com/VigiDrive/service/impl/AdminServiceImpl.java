package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.service.AdminService;
import com.VigiDrive.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private KeycloakService keycloakService;
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO registerAdmin(RegisterRequest newAdmin) {
        String keycloakId = keycloakService.createUser(newAdmin, Role.ADMIN);

        if (keycloakId == null || keycloakId.isEmpty()) {
            throw new RuntimeException("Registration failed.");
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
}
