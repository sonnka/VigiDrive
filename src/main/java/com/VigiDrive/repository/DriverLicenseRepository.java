package com.VigiDrive.repository;

import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long> {

    Optional<DriverLicense> findDriverLicenseByDriver(Driver driver);

    void deleteAllByDriver(Driver driver);
}