package com.VigiDrive.service;

import com.VigiDrive.exceptions.MailException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.UpdateAdminRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.DatabaseHistoryDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;

import java.util.List;

public interface AdminService {

    void addAdmin(String email, Long adminId, String newAdminEmail)
            throws UserException, SecurityException, MailException;

    List<AdminDTO> getApprovedAdmins(String email) throws UserException;

    List<AdminDTO> getNotApprovedAdmins(String email) throws UserException;

    void approveAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException;

    void declineAdmin(String email, Long adminId, Long newAdminId) throws UserException, MailException;

    void updateAdmin(String email, Long adminId, UpdateAdminRequest updatedAdmin) throws UserException;

    void exportDatabase(String email, Long adminId);

    void importDatabase(String email, Long adminId);

    List<DatabaseHistoryDTO> getWeekDatabaseHistory(String email, Long adminId) throws UserException;

    List<DatabaseHistoryDTO> getWeekDatabaseHistory();

    List<DatabaseHistoryDTO> getMonthDatabaseHistory();

    void deleteDriver(String email, Long adminId, Long driverId) throws UserException, SecurityException;

    void deleteManager(String email, Long adminId, Long managerId) throws UserException, SecurityException;

    List<ShortDriverDTO> getDrivers(String email, Long adminId) throws UserException;

    List<ManagerDTO> getManagers(String email, Long adminId) throws UserException;
}
