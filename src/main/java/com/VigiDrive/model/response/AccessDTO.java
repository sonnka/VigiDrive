package com.VigiDrive.model.response;

import com.VigiDrive.model.enums.TimeDuration;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AccessDTO {

    private Long id;

    private String driverEmail;

    private String managerEmail;

    private LocalDateTime startDateOfAccess;

    private LocalDateTime endDateOfAccess;

    private TimeDuration accessDuration;

    private Boolean isActive;

    private Boolean isExpiring;

}
