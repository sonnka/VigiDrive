package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Driver;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DriverDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String emergencyContact;

    public DriverDTO(Driver driver) {
        this.id = driver.getId();
        this.firstName = driver.getFirstName();
        this.lastName = driver.getLastName();
        this.email = driver.getEmail();
        this.avatar = driver.getAvatar();
        this.dateOfBirth = driver.getDateOfBirth();
        this.phoneNumber = driver.getPhoneNumber();
        this.emergencyContact = driver.getEmergencyContact();
    }

}
