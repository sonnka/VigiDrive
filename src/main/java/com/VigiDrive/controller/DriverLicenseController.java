package com.VigiDrive.controller;

import com.VigiDrive.exceptions.DriverLicenseException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;
import com.VigiDrive.service.DriverLicenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class DriverLicenseController {

    private DriverLicenseService driverLicenseService;

    @GetMapping("/drivers/{driver-id}/driver-license")
    public DriverLicenseDTO getDriverLicense(Authentication auth,
                                             @PathVariable("driver-id") Long driverId)
            throws DriverLicenseException, UserException {
        return driverLicenseService.getDriverLicense(auth, driverId);
    }

    @PostMapping("/drivers/{driver-id}/driver-license")
    public DriverLicenseDTO addDriverLicense(Authentication auth,
                                             @PathVariable("driver-id") Long driverId,
                                             @RequestBody @Valid DriverLicenseRequest driverLicense)
            throws DriverLicenseException, UserException {
        return driverLicenseService.addDriverLicense(auth, driverId, driverLicense);
    }
}
