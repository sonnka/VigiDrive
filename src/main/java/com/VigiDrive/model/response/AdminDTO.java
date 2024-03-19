package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.Admin;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private LocalDateTime dateOfApproving;

    private boolean chiefAdmin;

    private boolean approved;

    public AdminDTO(Admin admin) {
        this.id = admin.getId();
        this.firstName = admin.getFirstName();
        this.lastName = admin.getLastName();
        this.email = admin.getEmail();
        this.avatar = admin.getAvatar();
        this.dateOfApproving = admin.getDateOfApproving();
        this.chiefAdmin = admin.isChiefAdmin();
        this.approved = admin.isApproved();
    }
}
