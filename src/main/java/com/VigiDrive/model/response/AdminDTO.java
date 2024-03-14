package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Admin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private Boolean isApproved;

    public AdminDTO(Admin admin) {
        this.id = admin.getId();
        this.firstName = admin.getFirstName();
        this.lastName = admin.getLastName();
        this.email = admin.getEmail();
        this.avatar = admin.getAvatar();
        this.isApproved = admin.isApproved();
    }
}
