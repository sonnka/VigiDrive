package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.*;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverService;
import com.VigiDrive.service.KeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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

    @Override
    public DriverDTO updateDriver(Long driverId, UpdateDriverRequest newDriver) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        driver.setFirstName(newDriver.getFirstName());
        driver.setLastName(newDriver.getLastName());
        driver.setDateOfBirth(LocalDate.parse(newDriver.getDateOfBirth()));
        driver.setPhoneNumber(newDriver.getPhoneNumber());
        driver.setAvatar(newDriver.getAvatar());

        return new DriverDTO(driverRepository.save(driver));
    }

    @Override
    public void delete(Long driverId) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        keycloakService.deleteUser(driver.getKeycloakId().toString());

        driverRepository.delete(driver);
    }

    @Override
    public FullDriverDTO getFullDriver(Long driverId) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        return toFullDriverDTO(driver);
    }

    @Override
    public ManagerDTO getDriverManager(Long driverId) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        return new ManagerDTO(driver.getManager());
    }

    @Override
    public void updateCurrentLocation(Long driverId, String currentLocation) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        driver.setCurrentLocation(currentLocation);

        driverRepository.save(driver);
    }

    @Override
    public void addEmergencyNumber(Long driverId, String emergencyNumber) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        driver.setEmergencyContact(emergencyNumber);

        driverRepository.save(driver);
    }

    @Override
    public List<ShortDriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream().map(ShortDriverDTO::new).toList();
    }

    @Override
    public List<ShortDriverDTO> getAllDriversByManager(Long managerId) {
        return driverRepository.findAllByManagerId(managerId).stream().map(ShortDriverDTO::new).toList();
    }


    private FullDriverDTO toFullDriverDTO(Driver driver) {
        return new FullDriverDTO(driver.getId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getEmail(),
                driver.getAvatar(),
                driver.getDateOfBirth(),
                driver.getPhoneNumber(),
                driver.getDestination(),
                driver.getCurrentLocation(),
                new ManagerDTO(driver.getManager()),
                new DriverLicenseDTO(driver.getLicense()));
    }
}
