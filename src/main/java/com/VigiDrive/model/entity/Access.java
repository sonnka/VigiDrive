package com.VigiDrive.model.entity;

import com.VigiDrive.model.enums.TimeDuration;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Column(name = "start_date_of_access")
    private LocalDateTime startDateOfAccess;

    @Column(name = "end_date_of_access")
    private LocalDateTime endDateOfAccess;

    @Column(name = "access_duration")
    @Enumerated(EnumType.STRING)
    private TimeDuration accessDuration;

    @Column(name = "is_new")
    private boolean isNew;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_expiring")
    private boolean isExpiring;

    public Access() {
        this.isActive = false;
        this.isExpiring = false;
    }
}
