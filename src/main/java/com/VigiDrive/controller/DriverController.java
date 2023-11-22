package com.VigiDrive.controller;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.RegisterRequest;
import com.VigiDrive.model.request.UpdateDriverRequest;
import com.VigiDrive.model.response.DriverDTO;
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

    @PatchMapping("/api/drivers/{driver-id}")
    public DriverDTO updateDriver(@PathVariable("driver-id") Long driverId, @RequestBody @Valid UpdateDriverRequest driver)
            throws UserException {
        return driverService.updateDriver(driverId, driver);
    }
}
