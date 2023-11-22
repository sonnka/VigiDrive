package com.VigiDrive.controller;

import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;
import com.VigiDrive.service.DriverLicenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class DriverLicenseController {

    private DriverLicenseService driverLicenseService;

    @GetMapping("/drivers/{driver-id}/driver-license")
    public DriverLicenseDTO getDriverLicense(@PathVariable("driver-id") Long driverId) {
        return driverLicenseService.getDriverLicense(driverId);
    }

    @PostMapping("/drivers/{driver-id}/driver-license")
    public DriverLicenseDTO addDriverLicense(@PathVariable("driver-id") Long driverId,
                                             @RequestBody @Valid DriverLicenseRequest driverLicense) {
        return driverLicenseService.addDriverLicense(driverId, driverLicense);
    }
}
