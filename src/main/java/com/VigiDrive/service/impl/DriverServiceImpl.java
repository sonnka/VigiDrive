package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.exceptions.UserException.UserExceptionProfile;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverService;
import com.VigiDrive.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;
    private KeycloakService keycloakService;
    private PasswordEncoder passwordEncoder;

    @Override
    public DriverDTO registerDriver(RegisterRequest newDriver) {
        String keycloakId = keycloakService.createUser(newDriver, Role.DRIVER);

        if (keycloakId == null || keycloakId.isEmpty()) {
            throw new RuntimeException("Registration failed.");
        }

        Driver driver = Driver.builder()
                .email(newDriver.getEmail())
                .firstName(newDriver.getFirstName())
                .lastName(newDriver.getLastName())
                .keycloakId(UUID.fromString(keycloakId))
                .password(passwordEncoder.encode(newDriver.getPassword()))
                .role(Role.DRIVER)
                .build();

        return new DriverDTO(driverRepository.save(driver));
    }

    public DriverDTO updateDriver(Long id, UpdateDriverRequest newDriver) throws UserException {
        var driver = driverRepository.findById(id).orElseThrow(
                () -> new UserException(UserExceptionProfile.USER_NOT_FOUND));

        if (newDriver != null) {
            driver.setFirstName(newDriver.getFirstName());
            driver.setLastName(newDriver.getLastName());
            driver.setDateOfBirth(newDriver.getDateOfBirth());
            driver.setCountryCode(newDriver.getCountryCode());
            driver.setPhoneNumber(newDriver.getPhoneNumber());
            driver.setEmergencyContact(newDriver.getEmergencyContact());
        }

        return new DriverDTO(driverRepository.save(driver));
    }

}
