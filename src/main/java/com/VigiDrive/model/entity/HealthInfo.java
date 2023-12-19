package com.VigiDrive.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "health")
public class HealthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "stress_level")
    private Double stressLevel;

    @Column(name = "concentration_level")
    private Double concentrationLevel;

    @Column(name = "sleepiness_level")
    private Double sleepinessLevel;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JsonIgnore
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
