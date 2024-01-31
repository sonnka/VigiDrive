package com.VigiDrive.controller;

import com.VigiDrive.exceptions.AmazonException;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ManagerController {

    private ManagerService managerService;

    @PostMapping("/register/manager")
    public ResponseEntity<ManagerDTO> register(@Valid @RequestBody RegisterRequest newManager) throws SecurityException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(managerService.registerManager(newManager));
    }

    @PatchMapping("/managers/{manager-id}")
    public ManagerDTO updateManager(Authentication auth,
                                    @PathVariable("manager-id") Long managerId,
                                    @RequestBody @Valid UpdateManagerRequest manager) throws UserException {
        return managerService.updateManager(auth, managerId, manager);
    }

    @PostMapping("/managers/{manager-id}/avatar")
    public ManagerDTO uploadAvatar(Authentication auth,
                                   @PathVariable("manager-id") Long managerId,
                                   @RequestBody MultipartFile avatar) throws UserException, AmazonException {
        return managerService.uploadAvatar(auth, managerId, avatar);
    }

    @DeleteMapping("/managers/{manager-id}")
    public void deleteManager(Authentication auth,
                              @PathVariable("manager-id") Long managerId) throws SecurityException, UserException {
        managerService.delete(auth, managerId);
    }

    @GetMapping("/managers/{manager-id}")
    public FullManagerDTO getManager(Authentication auth,
                                     @PathVariable("manager-id") Long managerId) throws UserException {
        return managerService.getManager(auth, managerId);
    }

    @GetMapping("/managers/{manager-id}/drivers")
    public List<ShortDriverDTO> getDrivers(Authentication auth,
                                           @PathVariable("manager-id") Long managerId) throws UserException {
        return managerService.getDrivers(auth, managerId);
    }

    @GetMapping("/managers/{manager-id}/drivers/{driver-id}")
    public FullDriverDTO getDriver(Authentication auth,
                                   @PathVariable("manager-id") Long managerId,
                                   @PathVariable("driver-id") Long driverId) throws UserException {
        return managerService.getDriver(auth, managerId, driverId);
    }

    @PatchMapping("/managers/{manager-id}/drivers/{driver-id}/{destination}")
    public void setDestinationForDriver(Authentication auth,
                                        @PathVariable("manager-id") Long managerId,
                                        @PathVariable("driver-id") Long driverId,
                                        @PathVariable("destination") String destination) throws UserException {
        managerService.setDestinationForDriver(auth, managerId, driverId, destination);
    }
}
