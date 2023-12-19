package com.VigiDrive.controller;

import com.VigiDrive.exceptions.SecurityException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.model.response.FullDriverDTO;
import com.VigiDrive.model.response.ManagerDTO;
import com.VigiDrive.service.DriverService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class DriverController {

    private DriverService driverService;

    @PostMapping("/register/driver")
    public ResponseEntity<DriverDTO> register(@Valid @RequestBody RegisterRequest newDriver) throws SecurityException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(driverService.registerDriver(newDriver));
    }

    @PatchMapping("/drivers/{driver-id}")
    public DriverDTO updateDriver(Authentication auth,
                                  @PathVariable("driver-id") Long driverId,
                                  @RequestBody @Valid UpdateDriverRequest driver) throws UserException {
        return driverService.updateDriver(auth, driverId, driver);
    }

    @DeleteMapping("/drivers/{driver-id}")
    public void deleteDriver(Authentication auth,
                             @PathVariable("driver-id") Long driverId) throws SecurityException, UserException {
        driverService.delete(auth, driverId);
    }

    @GetMapping("/drivers/{driver-id}")
    public FullDriverDTO getDriver(Authentication auth,
                                   @PathVariable("driver-id") Long driverId) throws UserException {
        return driverService.getFullDriver(auth, driverId);
    }

    @GetMapping("/drivers/{driver-id}/manager")
    public ManagerDTO getDriverManager(Authentication auth,
                                       @PathVariable("driver-id") Long driverId) throws UserException {
        return driverService.getDriverManager(auth, driverId);
    }

    @PatchMapping("/drivers/{driver-id}/currentLocation/{currentLocation}")
    public void updateCurrentLocation(Authentication auth,
                                      @PathVariable("driver-id") Long driverId,
                                      @PathVariable("currentLocation") String currentLocation)
            throws UserException {
        driverService.updateCurrentLocation(auth, driverId, currentLocation);
    }

    @PatchMapping("/drivers/{driver-id}/emergency-number/{number}")
    public void addEmergencyNumber(Authentication auth,
                                   @PathVariable("driver-id") Long driverId,
                                   @PathVariable("number") String emergencyNumber) throws UserException {
        driverService.addEmergencyNumber(auth, driverId, emergencyNumber);
    }
}
