package com.VigiDrive.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FullDriverDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String destination;

    private String currentLocation;

    private ManagerDTO manager;

    private DriverLicenseDTO license;

    private List<SituationDTO> currentSituation;

    private HealthInfoDTO currentHealthInfo;
}
