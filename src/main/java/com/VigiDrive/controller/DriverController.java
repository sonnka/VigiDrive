package com.VigiDrive.controller;

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
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class DriverController {

    private DriverService driverService;

    @PostMapping("/register/driver")
    public ResponseEntity<DriverDTO> register(@Valid @RequestBody RegisterRequest newDriver) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(driverService.registerDriver(newDriver));
    }

    @PatchMapping("/drivers/{driver-id}")
    public DriverDTO updateDriver(@PathVariable("driver-id") Long driverId,
                                  @RequestBody @Valid UpdateDriverRequest driver) {
        return driverService.updateDriver(driverId, driver);
    }

    @DeleteMapping("/drivers/{driver-id}")
    public void deleteDriver(@PathVariable("driver-id") Long driverId) {
        driverService.delete(driverId);
    }

    @GetMapping("/drivers/{driver-id}")
    public FullDriverDTO getDriver(@PathVariable("driver-id") Long driverId) {
        return driverService.getFullDriver(driverId);
    }

    @GetMapping("/drivers/{driver-id}/manager")
    public ManagerDTO getDriverManager(@PathVariable("driver-id") Long driverId) {
        return driverService.getDriverManager(driverId);
    }

    @PatchMapping("/drivers/{driver-id}/currentLocation")
    public void updateCurrentLocation(@PathVariable("driver-id") Long driverId,
                                      @RequestBody String currentLocation) {
        driverService.updateCurrentLocation(driverId, currentLocation);
    }

    @PatchMapping("/drivers/{driver-id}/emergency-number")
    public void addEmergencyNumber(@PathVariable("driver-id") Long driverId,
                                   @RequestBody String emergencyNumber) {
        driverService.addEmergencyNumber(driverId, emergencyNumber);
    }
}
