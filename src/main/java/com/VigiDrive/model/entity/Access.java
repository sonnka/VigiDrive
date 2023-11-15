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

    @Column(name = "date_of_access")
    private LocalDateTime dateOfAccess;

    @Column(name = "access_duration")
    private TimeDuration accessDuration;

    @Column(name = "is_active")
    private Boolean isActive;

    public Access(){
        this.isActive = true;
    }
}
