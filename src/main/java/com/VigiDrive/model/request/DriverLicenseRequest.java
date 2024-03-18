package com.VigiDrive.model.request;

import com.VigiDrive.validation.Date;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DriverLicenseRequest {

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 9)
    private String number;

    @NotNull
    @NotEmpty
    @Date
    private String dateTo;
}
