package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "drivers")
public class Driver extends User {

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "destination")
    private String destination;

    @Column(name = "current_location")
    private String currentLocation;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "driver", orphanRemoval = true)
    private DriverLicense license;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver", orphanRemoval = true)
    @JsonIgnore
    @Column(name = "accesses")
    private List<Access> accesses = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver", orphanRemoval = true)
    @JsonIgnore
    @Column(name = "situations")
    private List<Situation> situations = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver", orphanRemoval = true)
    @JsonIgnore
    @Column(name = "health_info")
    private List<HealthInfo> healthInfo = new ArrayList<>();

    public Driver() {
        this.setRole(Role.DRIVER);
    }
}
