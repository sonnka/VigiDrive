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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AdminController {

    private AdminService adminService;

    @PostMapping("/register/admin")
    public ResponseEntity<AdminDTO> register(@Valid @RequestBody RegisterRequest newAdmin) throws SecurityException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminService.registerAdmin(newAdmin));
    }

    @GetMapping("/admins/{admin-id}/drivers")
    public List<ShortDriverDTO> getDrivers(Authentication auth,
                                           @PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getDrivers(auth, adminId);
    }

    @GetMapping("/admins/{admin-id}/managers")
    public List<ManagerDTO> getManagers(Authentication auth,
                                        @PathVariable("admin-id") Long adminId) throws UserException {
        return adminService.getManagers(auth, adminId);
    }

    @DeleteMapping("/admins/{admin-id}/drivers/{driver-id}")
    public void deleteDriver(Authentication auth,
                             @PathVariable("admin-id") Long adminId,
                             @PathVariable("driver-id") Long driverId) throws SecurityException, UserException {
        adminService.deleteDriver(auth, adminId, driverId);
    }

    @DeleteMapping("/admins/{admin-id}/managers/{manager-id}")
    public void deleteManager(Authentication auth,
                              @PathVariable("admin-id") Long adminId,
                              @PathVariable("manager-id") Long managerId) throws SecurityException, UserException {
        adminService.deleteManager(auth, adminId, managerId);
    }

}
