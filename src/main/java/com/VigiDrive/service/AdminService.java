package com.VigiDrive.service;

import com.VigiDrive.exceptions.DatabaseException;
import com.VigiDrive.exceptions.MailException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.UpdateAdminRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.DatabaseHistoryDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface AdminService {

    AdminDTO getAdmin(String email, Long adminId) throws UserException;

    void addAdmin(String email, Long adminId, String newAdminEmail)
            throws UserException, SecurityException, MailException;

    List<AdminDTO> getApprovedAdmins(String email) throws UserException;

    List<AdminDTO> getNotApprovedAdmins(String email) throws UserException;

    void approveAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException;

    void declineAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException;

    void updateAdmin(String email, Long adminId, UpdateAdminRequest updatedAdmin) throws UserException;

    void deleteDriver(String email, Long adminId, Long driverId) throws UserException, SecurityException;

    void deleteManager(String email, Long adminId, Long managerId) throws UserException, SecurityException;

    List<ShortDriverDTO> getDrivers(String email, Long adminId) throws UserException;

    List<ManagerDTO> getManagers(String email, Long adminId) throws UserException;

    List<DatabaseHistoryDTO> getWeekDatabaseHistory(String email, Long adminId) throws UserException;

    void exportDatabase(String email, Long adminId, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException, UserException;

    void importDatabase(String email, Long adminId, MultipartFile file)
            throws SQLException, ClassNotFoundException, IOException, UserException, DatabaseException;
}
