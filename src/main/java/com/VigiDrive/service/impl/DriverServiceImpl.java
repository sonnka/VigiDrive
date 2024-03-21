package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.AmazonException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.*;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.DriverService;
import com.VigiDrive.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final UserRepository userRepository;
    private DriverRepository driverRepository;
    private PasswordEncoder passwordEncoder;
    private AmazonClient amazonClient;
    private AuthUtil authUtil;

    @Override
    public DriverDTO registerDriver(RegisterRequest newDriver) throws SecurityException {

        var user = userRepository.findByEmailIgnoreCase(newDriver.getEmail()).orElse(null);

        if (user != null) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.EMAIL_OCCUPIED);
        }

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
    @Transactional
    public DriverDTO updateDriver(String email, Long driverId, UpdateDriverRequest newDriver)
            throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        if (newDriver.getAvatar() != null && !newDriver.getAvatar().isEmpty()) {
            driver.setAvatar(newDriver.getAvatar());
        }

        driver.setFirstName(newDriver.getFirstName());
        driver.setLastName(newDriver.getLastName());
        driver.setDateOfBirth(LocalDate.parse(newDriver.getDateOfBirth()));
        driver.setPhoneNumber(newDriver.getPhoneNumber());

        return new DriverDTO(driverRepository.save(driver));
    }

    @Override
    @Transactional
    public DriverDTO uploadAvatar(String email, Long driverId, MultipartFile avatar)
            throws UserException, AmazonException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        if (driver.getAvatar() != null && !driver.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(driver.getAvatar());
        }

        String fileName = amazonClient.uploadFile(avatar);

        driver.setAvatar(fileName);

        return new DriverDTO(driverRepository.save(driver));
    }

    @Override
    @Transactional
    public void delete(String email, Long driverId) throws UserException, SecurityException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByAdmin(email, driverId);

        delete(driver);
    }

    @Override
    @Transactional
    public void delete(Driver driver) {
        if (driver.getAvatar() != null && !driver.getAvatar().isEmpty()) {
            // amazonClient.deleteFileFromS3Bucket(driver.getAvatar());
        }

        driver.setManager(null);
        driverRepository.save(driver);

        driverRepository.delete(driver);
    }

    @Override
    public FullDriverDTO getFullDriver(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        return toFullDriverDTO(driver);
    }

    @Override
    public ManagerDTO getDriverManager(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        if (driver.getManager() == null) {
            return null;
        }

        return new ManagerDTO(driver.getManager());
    }

    @Override
    public void updateCurrentLocation(String email, Long driverId, String currentLocation) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        driver.setCurrentLocation(currentLocation);

        driverRepository.save(driver);
    }

    @Override
    public void addEmergencyNumber(String email, Long driverId, String emergencyNumber) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        driver.setEmergencyContact(emergencyNumber);

        driverRepository.save(driver);
    }

    @Override
    public List<ShortDriverDTO> getAllDriversByManager(String email, Long managerId) throws UserException {
        authUtil.findDriverByEmailAndIdAndCheckByManager(email, managerId);

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
