package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
//@AllArgsConstructor
@Table(name = "admins")
public class Admin extends User{


    public Admin(){
        this.setRole(Role.ADMIN);
    }
}
