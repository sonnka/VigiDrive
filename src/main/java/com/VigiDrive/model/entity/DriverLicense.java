package com.VigiDrive.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "licenses")
public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "date_to")
    private Date dateTo;

    @OneToOne(mappedBy = "license")
    private Driver driver;
}
