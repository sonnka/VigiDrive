package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.DriverLicenseException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.DriverLicense;
import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;
import com.VigiDrive.repository.DriverLicenseRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverLicenseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class DriverLicenseServiceImpl implements DriverLicenseService {

    private DriverLicenseRepository driverLicenseRepository;
    private DriverRepository driverRepository;

    @Override
    public DriverLicenseDTO getDriverLicense(Long driverId) throws UserException, DriverLicenseException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        var driverLicense = driverLicenseRepository.findDriverLicenseByDriver(driver)
                .orElseThrow(() -> new DriverLicenseException(
                        DriverLicenseException.DriverLicenseExceptionProfile.DRIVER_LICENSE_NOT_FOUND));

        return new DriverLicenseDTO(driverLicense);
    }

    @Override
    public DriverLicenseDTO addDriverLicense(Long driverId, DriverLicenseRequest driverLicense) throws UserException, DriverLicenseException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        driverLicenseRepository.deleteAllByDriver(driver);

        LocalDate date = null;

        try {
            date = LocalDate.parse(driverLicense.getDateTo(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            throw new DriverLicenseException(DriverLicenseException.DriverLicenseExceptionProfile.INVALID_DATE);
        }

        if (date.isBefore(LocalDate.now(Clock.systemUTC()))) {
            throw new DriverLicenseException(DriverLicenseException.DriverLicenseExceptionProfile.IS_EXPIRED);
        }

        return new DriverLicenseDTO(driverLicenseRepository.save(
                DriverLicense.builder()
                        .number(driverLicense.getNumber())
                        .dateTo(date)
                        .driver(driver)
                        .build())
        );
    }
}
