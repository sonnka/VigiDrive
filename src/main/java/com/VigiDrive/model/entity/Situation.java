package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.SituationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "situations")
public class Situation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "situation_id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start")
    private LocalDateTime start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end")
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SituationType type;

    @Column(name = "description")
    private String description;

    @Column(name = "video")
    private String video;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
