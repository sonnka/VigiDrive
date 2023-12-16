package com.VigiDrive.model.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class HealthInfoRequest {

    @Positive
    private Double stressLevel;

    @Positive
    private Double concentrationLevel;

    @Positive
    private Double sleepinessLevel;
}
