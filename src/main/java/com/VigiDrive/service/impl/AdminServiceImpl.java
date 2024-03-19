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
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.repository.UserRepository;
import com.VigiDrive.service.AdminService;
import com.VigiDrive.service.DatabaseHistoryService;
import com.VigiDrive.service.FileService;
import com.VigiDrive.service.MailService;
import com.VigiDrive.util.AuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private AdminRepository adminRepository;
    private DriverRepository driverRepository;
    private ManagerRepository managerRepository;

    private PasswordEncoder passwordEncoder;
    private AmazonClient amazonClient;

    private AuthUtil authUtil;
    private MailService mailService;
    private FileService fileService;
    private DatabaseHistoryService databaseHistoryService;


    @Override
    public void addAdmin(String email, Long adminId, String newAdminEmail)
            throws UserException, SecurityException, MailException {
        authUtil.checkAdminByEmailAndId(email, adminId);

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
        authUtil.checkAdminByEmailAndChief(email, adminId);

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
        authUtil.checkAdminByEmailAndChief(email, adminId);

        var newAdmin = adminRepository.findById(newAdminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        adminRepository.delete(newAdmin);

        mailService.sendNotApprovedAdminMessage(newAdmin.getEmail());
    }

    @Override
    public void updateAdmin(String email, Long adminId, UpdateAdminRequest updatedAdmin) throws UserException {
        var admin = authUtil.checkAdminByEmailAndId(email, adminId);

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
        authUtil.checkAdminByEmailAndId(email, adminId);

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
        authUtil.checkAdminByEmailAndId(email, adminId);

        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        if (manager.getAvatar() != null && !manager.getAvatar().isEmpty()) {
            amazonClient.deleteFileFromS3Bucket(manager.getAvatar());
        }

        managerRepository.delete(manager);
    }

    @Override
    public List<ShortDriverDTO> getDrivers(String email, Long adminId) throws UserException {
        authUtil.checkAdminByEmailAndId(email, adminId);

        return driverRepository.findAll().stream().map(ShortDriverDTO::new).toList();
    }

    @Override
    public List<ManagerDTO> getManagers(String email, Long adminId) throws UserException {
        authUtil.checkAdminByEmailAndId(email, adminId);

        return managerRepository.findAll().stream().map(ManagerDTO::new).toList();
    }

    @Override
    public List<DatabaseHistoryDTO> getWeekDatabaseHistory(String email, Long adminId) throws UserException {
        authUtil.checkAdminByEmailAndChief(email, adminId);

        return databaseHistoryService.getWeekDatabaseHistory();
    }

    @Override
    public void exportDatabase(String email, Long adminId, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException, UserException {
        var admin = authUtil.checkAdminByEmailAndId(email, adminId);

        fileService.generateDatabaseZipDump(response);

        databaseHistoryService.saveExportOperation(admin);
    }

    @Override
    public void importDatabase(String email, Long adminId, MultipartFile file)
            throws SQLException, ClassNotFoundException, IOException, UserException {
        authUtil.checkAdminByEmailAndId(email, adminId);

        String sql = new String(file.getBytes());

        fileService.importDatabase(sql);
    }
}
