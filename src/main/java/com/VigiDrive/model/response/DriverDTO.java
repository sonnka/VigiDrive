package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DriverDTO {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String avatar;

    private LocalDate dateOfBirth;

    private Sex sex;

    private String phoneNumber;

    private String emergencyContact;

    public DriverDTO(Driver driver) {
        this.id = driver.getId();
        this.name = driver.getName();
        this.surname = driver.getSurname();
        this.email = driver.getEmail();
        this.avatar = driver.getAvatar();
        this.dateOfBirth = driver.getDateOfBirth();
        this.sex = driver.getSex();
        this.phoneNumber = driver.getPhoneNumber();
        this.emergencyContact = driver.getEmergencyContact();
    }

}
