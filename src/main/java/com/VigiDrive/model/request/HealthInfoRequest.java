package com.VigiDrive.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HealthInfoRequest {

    @NotNull
    @NotEmpty
    private Double stressLevel;

    @NotNull
    @NotEmpty
    private Double concentrationLevel;

    @NotNull
    @NotEmpty
    private Double sleepinessLevel;
}
