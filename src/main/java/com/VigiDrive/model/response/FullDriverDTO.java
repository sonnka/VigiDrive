package com.VigiDrive.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FullDriverDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String destination;

    private String currentLocation;

    private String emergencyContact;

    private ManagerDTO manager;

    private DriverLicenseDTO license;
}
