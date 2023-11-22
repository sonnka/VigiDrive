package com.VigiDrive.model.request;

import com.VigiDrive.validation.Duration;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExtendAccessRequest {
    @NotNull
    @NotEmpty
    @Duration
    private String accessDuration;
}
