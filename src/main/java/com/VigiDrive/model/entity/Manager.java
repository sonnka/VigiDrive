package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "managers")
public class Manager extends User {

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "manager", orphanRemoval = true)
    @JsonIgnore
    @Column(name = "accesses")
    private List<Access> accesses = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST},
            mappedBy = "manager")
    @JsonIgnore
    @Column(name = "drivers")
    private List<Driver> drivers = new ArrayList<>();

    public Manager() {
        this.setRole(Role.MANAGER);
    }
}
