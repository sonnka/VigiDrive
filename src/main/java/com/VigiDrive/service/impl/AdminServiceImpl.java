package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.DatabaseException;
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
import com.VigiDrive.service.*;
import com.VigiDrive.util.AuthUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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
    private AuthUtil authUtil;

    private MailService mailService;
    private FileService fileService;
    private DriverService driverService;
    private ManagerService managerService;
    private DatabaseHistoryService databaseHistoryService;


    @Override
    public AdminDTO getAdmin(String email, Long adminId) throws UserException {
        return new AdminDTO(authUtil.checkAdminByEmailAndId(email, adminId));
    }

    @Override
    public void addAdmin(String email, Long adminId, String newAdminEmail)
            throws UserException, SecurityException, MailException {
        var admin = authUtil.checkAdminByEmailAndId(email, adminId);

        var user = userRepository.findByEmailIgnoreCase(newAdminEmail).orElse(null);

        if (user != null) {
            throw new SecurityException(SecurityException.SecurityExceptionProfile.EMAIL_OCCUPIED);
        }

        Admin newAdmin = Admin.builder()
                .email(newAdminEmail)
                .role(Role.ADMIN)
                .addedBy(admin.getEmail())
                .dateOfAdding(LocalDateTime.now())
                .approved(false)
                .newAccount(true)
                .build();

        adminRepository.save(newAdmin);

        mailService.sendNewAdminMessage(newAdminEmail);
    }

    @Override
    public void approveAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException {
        authUtil.checkAdminByEmailAndChief(email, adminId);

        var newAdmin = adminRepository.findById(newAdminId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        if (Role.CHIEF_ADMIN.equals(newAdmin.getRole())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

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

        if (Role.CHIEF_ADMIN.equals(newAdmin.getRole())) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

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
        admin.setNewAccount(false);

        adminRepository.save(admin);
    }

    @Override
    public List<AdminDTO> getApprovedAdmins(String email) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        return adminRepository.findAllByApprovedTrueOrderByDateOfApproving().stream()
                .filter(a -> a.getRole().equals(Role.ADMIN) && !a.getEmail().equals(admin.getEmail()))
                .map(AdminDTO::new)
                .toList();
    }

    @Override
    public List<AdminDTO> getNotApprovedAdmins(String email) throws UserException {
        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND));

        return adminRepository.findAllByApprovedFalseOrderByDateOfAdding().stream()
                .filter(a -> a.getRole().equals(Role.ADMIN) && !a.getEmail().equals(admin.getEmail()))
                .map(AdminDTO::new)
                .toList();
    }

    @Override
    public void deleteDriver(String email, Long adminId, Long driverId)
            throws UserException {
        authUtil.checkAdminByEmailAndId(email, adminId);

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        driverService.delete(driver);
    }

    @Override
    public void deleteManager(String email, Long adminId, Long managerId)
            throws UserException {
        authUtil.checkAdminByEmailAndId(email, adminId);

        var manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.MANAGER_NOT_FOUND));

        managerService.delete(manager);
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

        databaseHistoryService.saveExportOperation(admin);

        fileService.generateDatabaseZipDump(response);
    }

    @Override
    public void importDatabase(String email, Long adminId, MultipartFile file)
            throws IOException, UserException, DatabaseException {
        var admin = authUtil.checkAdminByEmailAndId(email, adminId);
        if (file == null || file.isEmpty()) {
            throw new DatabaseException(DatabaseException.DatabaseExceptionProfile.INVALID_FILE);
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!"sql".equals(extension)) {
            throw new DatabaseException(DatabaseException.DatabaseExceptionProfile.INVALID_FILE_EXTENSION);
        }

        String sql = new String(file.getBytes());

        databaseHistoryService.saveImportOperation(admin);

        try {
            fileService.importDatabase(sql);
        } catch (Exception exception) {
            throw new DatabaseException(DatabaseException.DatabaseExceptionProfile.DATABASE_EXCEPTION);
        }
    }
}
