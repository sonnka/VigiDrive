package com.VigiDrive.service;

import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;

public interface DriverLicenseService {

    DriverLicenseDTO getDriverLicense(Long driverId);

    DriverLicenseDTO addDriverLicense(Long driverId, DriverLicenseRequest driverLicense);
}
