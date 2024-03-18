package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "admins")
public class Admin extends User {

    @Column(name = "date_of_approving")
    private LocalDateTime dateOfApproving;

    @Column(name = "is_approved")
    private boolean approved;

    @Column(name = "is_chief_admin")
    private boolean chiefAdmin;

    public Admin() {
        this.setRole(Role.ADMIN);
        this.setApproved(false);
        this.setChiefAdmin(false);
    }
}
