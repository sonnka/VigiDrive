package com.VigiDrive.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate dateTo;

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "user_id")
    private Driver driver;
}
