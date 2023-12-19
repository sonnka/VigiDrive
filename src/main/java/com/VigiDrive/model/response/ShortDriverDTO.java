package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Driver;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortDriverDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    public ShortDriverDTO(Driver driver) {
        this.id = driver.getId();
        this.firstName = driver.getFirstName();
        this.lastName = driver.getLastName();
        this.email = driver.getEmail();
        this.avatar = driver.getAvatar();
    }
}
