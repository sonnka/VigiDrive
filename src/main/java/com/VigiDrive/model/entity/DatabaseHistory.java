package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.DatabaseOperation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "histories")
public class DatabaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private DatabaseOperation operation;
}
