package com.VigiDrive.controller;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateManagerRequest;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.FullManagerDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.model.response.ShortDriverDTO;
import com.VigiDrive.service.ManagerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
public class ManagerController {

    private ManagerService managerService;

    @PostMapping("/register/manager")
    public ResponseEntity<ManagerDTO> register(@Valid @RequestBody RegisterRequest newManager) throws SecurityException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(managerService.registerManager(newManager));
    }

    @PatchMapping("/managers/{manager-id}")
    public ManagerDTO updateManager(@PathVariable("manager-id") Long managerId,
                                    @RequestBody @Valid UpdateManagerRequest manager) throws UserException {
        return managerService.updateManager(managerId, manager);
    }

    @DeleteMapping("/managers/{manager-id}")
    public void deleteManager(@PathVariable("manager-id") Long managerId) throws SecurityException, UserException {
        managerService.delete(managerId);
    }

    @GetMapping("/managers/{manager-id}")
    public FullManagerDTO getManager(@PathVariable("manager-id") Long managerId) throws UserException {
        return managerService.getManager(managerId);
    }

    @GetMapping("/managers/{manager-id}/drivers")
    public List<ShortDriverDTO> getDrivers(@PathVariable("manager-id") Long managerId) throws UserException {
        return managerService.getDrivers(managerId);
    }

    @GetMapping("/managers/{manager-id}/drivers/{driver-id}")
    public FullDriverDTO getDriver(@PathVariable("manager-id") Long managerId,
                                   @PathVariable("driver-id") Long driverId) throws UserException {
        return managerService.getDriver(managerId, driverId);
    }

    @PatchMapping("/managers/{manager-id}/drivers/{driver-id}/destination")
    public void setDestinationForDriver(@PathVariable("manager-id") Long managerId,
                                        @PathVariable("driver-id") Long driverId,
                                        @RequestBody String destination) throws UserException {
        managerService.setDestinationForDriver(managerId, driverId, destination);
    }
}
