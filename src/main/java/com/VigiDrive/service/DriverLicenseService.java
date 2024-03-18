package com.VigiDrive.service;

import com.VigiDrive.exceptions.DriverLicenseException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;

public interface DriverLicenseService {

    DriverLicenseDTO getDriverLicense(String email, Long driverId) throws UserException, DriverLicenseException;

    DriverLicenseDTO addDriverLicense(String email, Long driverId, DriverLicenseRequest driverLicense)
            throws UserException, DriverLicenseException;
}
