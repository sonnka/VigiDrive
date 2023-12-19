package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.DriverLicense;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DriverLicenseDTO {
    private Long id;

    private String number;

    private LocalDate dateTo;

    public DriverLicenseDTO(DriverLicense driverLicense) {
        this.id = driverLicense.getId();
        this.number = driverLicense.getNumber();
        this.dateTo = driverLicense.getDateTo();
    }
}
