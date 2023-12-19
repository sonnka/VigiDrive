package com.VigiDrive.service;

import com.VigiDrive.exceptions.DriverLicenseException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;
import org.springframework.security.core.Authentication;

public interface DriverLicenseService {

    DriverLicenseDTO getDriverLicense(Authentication auth, Long driverId) throws UserException, DriverLicenseException;

    DriverLicenseDTO addDriverLicense(Authentication auth, Long driverId, DriverLicenseRequest driverLicense)
            throws UserException, DriverLicenseException;
}
