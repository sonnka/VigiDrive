package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Manager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FullManagerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private Integer amountOfDrivers;

    private List<ShortDriverDTO> drivers;

    public FullManagerDTO(Manager manager) {
        this.id = manager.getId();
        this.firstName = manager.getFirstName();
        this.lastName = manager.getLastName();
        this.email = manager.getEmail();
        this.avatar = manager.getAvatar();
        this.amountOfDrivers = 0;
        if (manager.getDrivers() != null) {
            this.amountOfDrivers = manager.getDrivers().size();
        }
        this.drivers = manager.getDrivers() != null ? manager.getDrivers().stream().map(ShortDriverDTO::new).toList() : null;
    }
}
