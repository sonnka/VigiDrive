package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.Role;
import com.VigiDrive.model.enums.Sex;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "drivers")
public class Driver extends User{

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Manager manager;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "license_id", referencedColumnName = "license_id")
    private DriverLicense license;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver")
    @JsonIgnore
    @Column(name = "situations")
    private List<Situation> situations = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver")
    @JsonIgnore
    @Column(name = "health_info")
    private List<HealthInfo> healthInfo = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "driver")
    @JsonIgnore
    @Column(name = "recommendations")
    private List<Recommendation> recommendations = new ArrayList<>();

    public Driver(){
        this.setRole(Role.DRIVER);
    }
}
