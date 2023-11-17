package com.VigiDrive.controller;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.DriverRequest;
import com.VigiDrive.model.response.DriverDTO;
import com.VigiDrive.service.DriverService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DriverController {

    private DriverService driverService;

    @PatchMapping("/api/drivers/{driver-id}")
    public DriverDTO updateDriver(@PathVariable("driver-id") Long driverId, @RequestBody @Valid DriverRequest driver)
            throws UserException {
        return driverService.updateDriver(driverId, driver);
    }
}
