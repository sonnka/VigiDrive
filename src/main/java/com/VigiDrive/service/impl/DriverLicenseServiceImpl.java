package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.DriverLicense;
import com.VigiDrive.model.request.DriverLicenseRequest;
import com.VigiDrive.model.response.DriverLicenseDTO;
import com.VigiDrive.repository.DriverLicenseRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.service.DriverLicenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class DriverLicenseServiceImpl implements DriverLicenseService {

    private DriverLicenseRepository driverLicenseRepository;
    private DriverRepository driverRepository;

    @Override
    public DriverLicenseDTO getDriverLicense(Long driverId) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        var driverLicense = driverLicenseRepository.findDriverLicenseByDriver(driver)
                .orElseThrow(() -> new RuntimeException("Driver license not found!"));

        return new DriverLicenseDTO(driverLicense);
    }

    @Override
    public DriverLicenseDTO addDriverLicense(Long driverId, DriverLicenseRequest driverLicense) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        driverLicenseRepository.deleteAllByDriver(driver);

        LocalDate date = null;
        
        try {
            date = LocalDate.parse(driverLicense.getDateTo(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Date is invalid!");
        }

        if (date.isBefore(LocalDate.now(Clock.systemUTC()))) {
            throw new RuntimeException("Driver license is expired!");
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
