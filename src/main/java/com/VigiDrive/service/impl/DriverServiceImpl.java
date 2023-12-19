package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.*;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public DriverDTO registerDriver(RegisterRequest newDriver) {

        Driver driver = Driver.builder()
                .email(newDriver.getEmail())
                .firstName(newDriver.getFirstName())
                .lastName(newDriver.getLastName())
                .password(passwordEncoder.encode(newDriver.getPassword()))
                .role(Role.DRIVER)
                .build();

        return new DriverDTO(driverRepository.save(driver));
    }

    @Override
    public DriverDTO updateDriver(Authentication auth, Long driverId, UpdateDriverRequest newDriver)
            throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        driver.setFirstName(newDriver.getFirstName());
        driver.setLastName(newDriver.getLastName());
        driver.setDateOfBirth(LocalDate.parse(newDriver.getDateOfBirth()));
        driver.setPhoneNumber(newDriver.getPhoneNumber());
        driver.setAvatar(newDriver.getAvatar());

        return new DriverDTO(driverRepository.save(driver));
    }

    @Override
    public void delete(Authentication auth, Long driverId) throws UserException, SecurityException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        driverRepository.delete(driver);
    }

    @Override
    public FullDriverDTO getFullDriver(Authentication auth, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        return toFullDriverDTO(driver);
    }

    @Override
    public ManagerDTO getDriverManager(Authentication auth, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));
        if (driver.getManager() == null) {
            return null;
        }
        return new ManagerDTO(driver.getManager());
    }

    @Override
    public void updateCurrentLocation(Authentication auth, Long driverId, String currentLocation) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        driver.setCurrentLocation(currentLocation);

        driverRepository.save(driver);
    }

    @Override
    public void addEmergencyNumber(Authentication auth, Long driverId, String emergencyNumber) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        driver.setEmergencyContact(emergencyNumber);

        driverRepository.save(driver);
    }

    @Override
    public List<ShortDriverDTO> getAllDrivers(Authentication auth) {
        return driverRepository.findAll().stream().map(ShortDriverDTO::new).toList();
    }

    @Override
    public List<ShortDriverDTO> getAllDriversByManager(Authentication auth, Long managerId) {
        return driverRepository.findAllByManagerId(managerId).stream().map(ShortDriverDTO::new).toList();
    }


    private FullDriverDTO toFullDriverDTO(Driver driver) {
        ManagerDTO manager = null;
        DriverLicenseDTO driverLicense = null;

        if (driver.getManager() != null) {
            manager = new ManagerDTO(driver.getManager());
        }
        if (driver.getLicense() != null) {
            driverLicense = new DriverLicenseDTO(driver.getLicense());
        }
        return new FullDriverDTO(driver.getId(),
                driver.getFirstName(),
                driver.getLastName(),
                driver.getEmail(),
                driver.getAvatar(),
                driver.getDateOfBirth(),
                driver.getPhoneNumber(),
                driver.getDestination(),
                driver.getCurrentLocation(),
                driver.getEmergencyContact(),
                manager,
                driverLicense);
    }
}
