package com.VigiDrive.controller;

import com.VigiDrive.exceptions.MailException;
import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.UpdateAdminRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.DatabaseHistoryDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AdminController {

    private AdminService adminService;

    @GetMapping("/admins/{admin-id}")
    public AdminDTO getAdmin(Authentication auth,
                             @PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getAdmin(auth.getName(), adminId);
    }

    @GetMapping("/admins/{admin-id}/drivers")
    public List<ShortDriverDTO> getDrivers(Authentication auth,
                                           @PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getDrivers(auth.getName(), adminId);
    }

    @GetMapping("/admins/{admin-id}/managers")
    public List<ManagerDTO> getManagers(Authentication auth,
                                        @PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getManagers(auth.getName(), adminId);
    }

    @DeleteMapping("/admins/{admin-id}/drivers/{driver-id}")
    public void deleteDriver(Authentication auth,
                             @PathVariable("admin-id") Long adminId,
                             @PathVariable("driver-id") Long driverId) throws SecurityException, UserException {
        adminService.deleteDriver(auth.getName(), adminId, driverId);
    }

    @DeleteMapping("/admins/{admin-id}/managers/{manager-id}")
    public void deleteManager(Authentication auth,
                              @PathVariable("admin-id") Long adminId,
                              @PathVariable("manager-id") Long managerId) throws SecurityException, UserException {
        adminService.deleteManager(auth.getName(), adminId, managerId);
    }

    @GetMapping("/admins/approved")
    public List<AdminDTO> getApprovedAdmins(Authentication auth) throws UserException {
        return adminService.getApprovedAdmins(auth.getName());
    }

    @GetMapping("/admins/not-approved")
    public List<AdminDTO> getNotApprovedAdmins(Authentication auth) throws UserException {
        return adminService.getNotApprovedAdmins(auth.getName());
    }

    @PostMapping("/admins/{admin-id}/add/{email}")
    public void addAdmin(Authentication auth,
                         @PathVariable("admin-id") Long adminId,
                         @PathVariable("email") String email) throws UserException, SecurityException, MailException {
        adminService.addAdmin(auth.getName(), adminId, email);
    }

    @PostMapping("/admins/{admin-id}/approve/{new-admin-id}")
    public void approveAdmin(Authentication auth,
                             @PathVariable("admin-id") Long adminId,
                             @PathVariable("new-admin-id") Long newAdminId) throws UserException, MailException {
        adminService.approveAdmin(auth.getName(), adminId, newAdminId);
    }

    @PostMapping("/admins/{admin-id}/decline/{new-admin-id}")
    public void declineAdmin(Authentication auth,
                             @PathVariable("admin-id") Long adminId,
                             @PathVariable("new-admin-id") Long newAdminId) throws UserException, MailException {
        adminService.declineAdmin(auth.getName(), adminId, newAdminId);
    }

    @PatchMapping("/admins/{admin-id}")
    public void updateAdmin(Authentication auth,
                            @PathVariable("admin-id") Long adminId,
                            @RequestBody UpdateAdminRequest updatedAdmin) throws UserException {
        adminService.updateAdmin(auth.getName(), adminId, updatedAdmin);
    }

    @GetMapping("/admins/{admin-id}/db/history")
    public List<DatabaseHistoryDTO> getWeekDatabaseHistory(Authentication auth,
                                                           @PathVariable("admin-id") Long adminId)
            throws UserException {
        return adminService.getWeekDatabaseHistory(auth.getName(), adminId);
    }

    @GetMapping(value = "/admins/{admin-id}/db/export", produces = "application/zip")
    public void exportDatabase(Authentication auth,
                               @PathVariable("admin-id") Long adminId,
                               HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException, UserException {
        adminService.exportDatabase(auth.getName(), adminId, response);
    }

    @PostMapping("/admins/{admin-id}/db/import")
    public void importDatabase(Authentication auth,
                               @PathVariable("admin-id") Long adminId,
                               @RequestParam("file") MultipartFile file)
            throws SQLException, IOException, ClassNotFoundException, UserException {
        adminService.importDatabase(auth.getName(), adminId, file);
    }
}