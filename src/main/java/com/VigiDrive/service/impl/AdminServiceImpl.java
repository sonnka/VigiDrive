package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.MailException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.request.UpdateAdminRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.DatabaseHistoryDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.repository.*;
import com.VigiDrive.service.AdminService;
import com.VigiDrive.service.MailService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private AdminRepository adminRepository;
    private DriverRepository driverRepository;
    private ManagerRepository managerRepository;
    private DatabaseHistoryRepository databaseHistoryRepository;
    private PasswordEncoder passwordEncoder;
    private AmazonClient amazonClient;
    private MailService mailService;


    @Override
    public void addAdmin(String email, Long adminId, String newAdminEmail)
            throws UserException, SecurityException, MailException {
        checkAdminByEmailAndId(email, adminId);

        var user = userRepository.findByEmailIgnoreCase(newAdminEmail).orElse(null);

        if (user != null) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.EMAIL_OCCUPIED);
        }

        Admin newAdmin = Admin.builder()
                .email(newAdminEmail)
                .role(Role.ADMIN)
                .approved(false)
                .build();

        adminRepository.save(newAdmin);

        mailService.sendNewAdminMessage(newAdminEmail);
    }

    @Override
    public void approveAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException {
        checkAdminByEmailAndChief(email, adminId);

        var newAdmin = adminRepository.findById(newAdminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        var password = RandomStringUtils.random(7, true, true);

        newAdmin.setApproved(true);
        newAdmin.setDateOfApproving(LocalDateTime.now());
        newAdmin.setPassword(passwordEncoder.encode(password));

        adminRepository.save(newAdmin);

        mailService.sendApprovedAdminMessage(newAdmin.getEmail(), password);
    }

    @Override
    public void declineAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException {
        checkAdminByEmailAndChief(email, adminId);

        var newAdmin = adminRepository.findById(newAdminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        adminRepository.delete(newAdmin);

        mailService.sendNotApprovedAdminMessage(newAdmin.getEmail());
    }

    @Override
    public void updateAdmin(String email, Long adminId, UpdateAdminRequest updatedAdmin) throws UserException {
        var admin = checkAdminByEmailAndId(email, adminId);

        if (!admin.isApproved()) {
            throw new UserException(UserException.UserExceptionProfile.SOMETHING_WRONG);
        }

        admin.setAvatar(updatedAdmin.getAvatar());
        admin.setFirstName(updatedAdmin.getFirstName());
        admin.setLastName(updatedAdmin.getLastName());
        admin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));

        adminRepository.save(admin);
    }

    @Override
    public void exportDatabase(String email, Long adminId) {

    }

    @Override
    public void importDatabase(String email, Long adminId) {

    }

    @Override
    public List<DatabaseHistoryDTO> getWeekDatabaseHistory(String email, Long adminId) throws UserException {
        checkAdminByEmailAndChief(email, adminId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        return databaseHistoryRepository.findAllByTimeAfterOrderByTime(startOfWeek)
                .stream()
                .map(DatabaseHistoryDTO::new)
                .toList();
    }

    @Override
    public List<DatabaseHistoryDTO> getWeekDatabaseHistory() {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        return databaseHistoryRepository.findAllByTimeAfterOrderByTime(startOfWeek)
                .stream()
                .map(DatabaseHistoryDTO::new)
                .toList();
    }

    @Override
    public List<DatabaseHistoryDTO> getMonthDatabaseHistory() {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

        return databaseHistoryRepository.findAllByTimeAfterOrderByTime(startOfMonth)
                .stream()
                .map(DatabaseHistoryDTO::new)
                .toList();
    }

    @Override
    public List<AdminDTO> getApprovedAdmins(String email) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.isChiefAdmin()) {
            throw new UserException(UserException.UserExceptionProfile.NOT_CHIEF_ADMIN);
        }

        return adminRepository.findAllByApproved(true).stream()
                .map(AdminDTO::new)
                .toList();
    }

    @Override
    public List<AdminDTO> getNotApprovedAdmins(String email) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.isChiefAdmin()) {
            throw new UserException(UserException.UserExceptionProfile.NOT_CHIEF_ADMIN);
        }

        return adminRepository.findAllByApproved(false).stream()
                .map(AdminDTO::new)
                .toList();
    }

    @Override
    public void deleteDriver(String email, Long adminId, Long driverId)
            throws UserException {
        checkAdminByEmailAndId(email, adminId);

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (driver.getAvatar() != null && !driver.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(driver.getAvatar());
        }

        driverRepository.delete(driver);
    }

    @Override
    public void deleteManager(String email, Long adminId, Long managerId)
            throws UserException {
        checkAdminByEmailAndId(email, adminId);

        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (manager.getAvatar() != null && !manager.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(manager.getAvatar());
        }

        managerRepository.delete(manager);
    }

    @Override
    public List<ShortDriverDTO> getDrivers(String email, Long adminId) throws UserException {
        checkAdminByEmailAndId(email, adminId);

        return driverRepository.findAll().stream().map(ShortDriverDTO::new).toList();
    }

    @Override
    public List<ManagerDTO> getManagers(String email, Long adminId) throws UserException {
        checkAdminByEmailAndId(email, adminId);

        return managerRepository.findAll().stream().map(ManagerDTO::new).toList();
    }

    private Admin checkAdminByEmailAndId(String email, Long adminId) throws UserException {
        var admin = adminRepository.findById(adminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.getEmail().equals(email)) {
            throw new UserException(UserException.UserExceptionProfile.EMAIL_MISMATCH);
        }

        return admin;
    }

    private void checkAdminByEmailAndChief(String email) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.isChiefAdmin()) {
            throw new UserException(UserException.UserExceptionProfile.NOT_CHIEF_ADMIN);
        }
    }

    private void checkAdminByEmailAndChief(String email, Long adminId) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (!admin.getId().equals(adminId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        if (!admin.isChiefAdmin()) {
            throw new UserException(UserException.UserExceptionProfile.NOT_CHIEF_ADMIN);
        }
    }
}
