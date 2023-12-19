package com.VigiDrive.model.request;

import com.VigiDrive.validation.Duration;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccessRequest {

    @NotNull
    @NotEmpty
    @Email
    private String driverEmail;

    @NotNull
    @NotEmpty
    @Duration
    private String accessDuration;
}
