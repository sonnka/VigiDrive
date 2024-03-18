package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.AmazonException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.DriverService;
import com.VigiDrive.service.ManagerService;
import com.VigiDrive.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final UserRepository userRepository;
    private ManagerRepository managerRepository;
    private DriverService driverService;
    private DriverRepository driverRepository;
    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private AmazonClient amazonClient;

    @Override
    public ManagerDTO registerManager(RegisterRequest newManager) throws SecurityException {

        var user = userRepository.findByEmailIgnoreCase(newManager.getEmail()).orElse(null);

        if (user != null) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.EMAIL_OCCUPIED);
        }

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
    public List<ShortDriverDTO> getDrivers(String email, Long managerId) throws UserException {
        var manager = AuthUtil.findManagerByEmailAndId(email, managerId, managerRepository);

        return driverService.getAllDriversByManager(email, manager.getId());
    }

    @Override
    public FullDriverDTO getDriver(String email, Long managerId, Long driverId) throws UserException {
        var manager = AuthUtil.findManagerByEmailAndId(email, managerId, managerRepository);

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getManager() == null || !Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        return driverService.getFullDriver(email, driverId);
    }

    @Override
    public ManagerDTO updateManager(String email, Long managerId, UpdateManagerRequest newManager)
            throws UserException {
        var manager = AuthUtil.findManagerByEmailAndId(email, managerId, managerRepository);

        manager.setFirstName(newManager.getFirstName());
        manager.setLastName(newManager.getLastName());

        if (newManager.getAvatar() != null && !newManager.getAvatar().isEmpty()) {
            manager.setAvatar(newManager.getAvatar());
        }

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    @Transactional
    public ManagerDTO uploadAvatar(String email, Long managerId, MultipartFile avatar)
            throws UserException, AmazonException {
        var manager = AuthUtil.findManagerByEmailAndId(email, managerId, managerRepository);

        if (manager.getAvatar() != null && !manager.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(manager.getAvatar());
        }

        String fileName = amazonClient.uploadFile(avatar);

        manager.setAvatar(fileName);

        return new ManagerDTO(managerRepository.save(manager));
    }

    @Override
    public FullManagerDTO getManager(String email, Long managerId) throws UserException {
        var manager = AuthUtil.findManagerByEmailAndId(email, managerId, managerRepository);

        return new FullManagerDTO(manager);
    }

    @Override
    public void setDestinationForDriver(String email, Long managerId, Long driverId, String destination)
            throws UserException {
        var manager = AuthUtil.findManagerByEmailAndId(email, managerId, managerRepository);

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getManager() == null || !Objects.equals(driver.getManager().getId(), manager.getId())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        driver.setDestination(destination);

        driverRepository.save(driver);
    }

    @Override
    @Transactional
    public void delete(String email, Long managerId) throws UserException {
        var manager = AuthUtil.findManagerByEmailAndIdAndCheckByAdmin(email, managerId, managerRepository,
                adminRepository);

        if (manager.getAvatar() != null && !manager.getAvatar().isEmpty()) {
            try {
                amazonClient.deleteFileFromS3Bucket(manager.getAvatar());
            } catch (Exception exception) {
                System.err.println(exception);
            }
        }

        for (Driver driver : manager.getDrivers()) {
            driver.setManager(null);
        }

        driverRepository.saveAll(manager.getDrivers());

        managerRepository.delete(manager);
    }
}
