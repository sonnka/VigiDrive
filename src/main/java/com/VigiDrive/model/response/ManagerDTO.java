package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private Integer amountOfDrivers;

    public ManagerDTO(Manager manager) {
        this.id = manager.getId();
        this.firstName = manager.getFirstName();
        this.lastName = manager.getLastName();
        this.email = manager.getEmail();
        this.avatar = manager.getAvatar();
        this.amountOfDrivers = 0;
        if (manager.getDrivers() != null) {
            this.amountOfDrivers = manager.getDrivers().size();
        }
    }
}
