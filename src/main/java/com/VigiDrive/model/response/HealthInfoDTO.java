package com.VigiDrive.model.response;

import com.VigiDrive.model.entity.HealthInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
public class HealthInfoDTO {

    private Long id;

    private ShortDriverDTO driver;

    private LocalDateTime time;

    private Double stressLevel;

    private Double concentrationLevel;

    private Double sleepinessLevel;

    private Double generalStatus;

    public HealthInfoDTO(HealthInfo health) {
        this.id = health.getId();
        this.driver = new ShortDriverDTO(health.getDriver());
        this.time = health.getTime();
        this.stressLevel = health.getStressLevel();
        this.concentrationLevel = health.getConcentrationLevel();
        this.sleepinessLevel = health.getSleepinessLevel();
        this.generalStatus = calculateGeneralStatus();
    }

    private Double calculateGeneralStatus() {
        double stressWeight = 0.2;
        double concentrationWeight = 0.6;
        double sleepinessWeight = 0.2;

        double general = ((stressWeight * (10 - stressLevel))
                + (concentrationWeight * concentrationLevel)
                + (sleepinessWeight * (10 - sleepinessLevel))) * 10;

        return BigDecimal.valueOf(general)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
