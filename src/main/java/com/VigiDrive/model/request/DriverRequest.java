package com.VigiDrive.model.request;

import com.VigiDrive.model.enums.CountryCode;
import com.VigiDrive.model.enums.Sex;
import com.VigiDrive.validation.Code;
import com.VigiDrive.validation.Date;
import com.VigiDrive.validation.PhoneNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DriverRequest {

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 24)
    private String name;

    @NotEmpty
    @NotNull
    @Size(min = 2, max = 24)
    private String surname;

    @NotNull
    @NotEmpty
    @Date
    private LocalDate dateOfBirth;

    @NotNull
    @NotEmpty
    @com.VigiDrive.validation.Sex
    private Sex sex;

    @NotNull
    @NotEmpty
    @Code
    private CountryCode countryCode;

    @NotNull
    @NotEmpty
    @PhoneNumber
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @PhoneNumber
    private String emergencyContact;
}
