package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.DriverLicenseException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.DriverLicense;
import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;
import com.VigiDrive.repository.DriverLicenseRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverLicenseService;
import com.VigiDrive.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@Transactional
public class DriverLicenseServiceImpl implements DriverLicenseService {

    private DriverLicenseRepository driverLicenseRepository;
    private DriverRepository driverRepository;
    private AuthUtil authUtil;

    @Override
    public DriverLicenseDTO getDriverLicense(String email, Long driverId)
            throws UserException, DriverLicenseException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        var driverLicense = driverLicenseRepository.findDriverLicenseByDriver(driver)
                .orElseThrow(() -> new DriverLicenseException(
                        DriverLicenseException.DriverLicenseExceptionProfile.DRIVER_LICENSE_NOT_FOUND));

        return new DriverLicenseDTO(driverLicense);
    }

    @Override
    @Transactional
    public DriverLicenseDTO addDriverLicense(String email, Long driverId, DriverLicenseRequest driverLicense)
            throws UserException, DriverLicenseException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        if (driver.getLicense() != null) {
            var licenseId = driver.getLicense().getId();
            driver.setLicense(null);
            driverRepository.save(driver);
            driverLicenseRepository.deleteById(licenseId);
        }

        LocalDate date;

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
