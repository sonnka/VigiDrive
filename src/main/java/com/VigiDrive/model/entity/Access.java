package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.TimeDuration;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@Table(name = "accesses")
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Long id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "start_date_of_access")
    private LocalDateTime startDateOfAccess;

    @Column(name = "end_date_of_access")
    private LocalDateTime endDateOfAccess;

    @Column(name = "access_duration")
    @Enumerated(EnumType.STRING)
    private TimeDuration accessDuration;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_expiring")
    private Boolean isExpiring;

    public Access() {
        this.isActive = false;
        this.isExpiring = false;
    }
}
