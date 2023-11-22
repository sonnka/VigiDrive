package com.VigiDrive.model.request;

import com.VigiDrive.model.enums.CountryCode;
import com.VigiDrive.validation.Code;
import com.VigiDrive.validation.Date;
import com.VigiDrive.validation.PhoneNumber;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UpdateDriverRequest {

    @Size(min = 2, max = 24)
    private String firstName;

    @Size(min = 2, max = 24)
    private String lastName;

    @Date
    private LocalDate dateOfBirth;

    @Code
    private CountryCode countryCode;

    @PhoneNumber
    private String phoneNumber;

    @PhoneNumber
    private String emergencyContact;
}
