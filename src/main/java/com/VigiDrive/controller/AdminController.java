package com.VigiDrive.controller;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.response.AdminDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.service.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping("/register/admin")
    public ResponseEntity<AdminDTO> register(@Valid @RequestBody RegisterRequest newAdmin) throws SecurityException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.registerAdmin(newAdmin));
    }

    @GetMapping("/admins/{admin-id}/drivers")
    public List<ShortDriverDTO> getDrivers(@PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getDrivers(adminId);
    }

    @GetMapping("/admins/{admin-id}/managers")
    public List<ManagerDTO> getManagers(@PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getManagers(adminId);
    }

    @DeleteMapping("/admins/{admin-id}/drivers/{driver-id}")
    public void deleteDriver(@PathVariable("admin-id") Long adminId,
                             @PathVariable("driver-id") Long driverId) throws SecurityException, UserException {
        adminService.deleteDriver(adminId, driverId);
    }

    @DeleteMapping("/admins/{admin-id}/managers/{manager-id}")
    public void deleteManager(@PathVariable("admin-id") Long adminId,
                              @PathVariable("manager-id") Long managerId) throws SecurityException, UserException {
        adminService.deleteManager(adminId, managerId);
    }

}
