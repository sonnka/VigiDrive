package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.HealthInfo;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.HealthInfoRepository;
import com.VigiDrive.service.HealthInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class HealthInfoServiceImpl implements HealthInfoService {

    private HealthInfoRepository healthInfoRepository;
    private DriverRepository driverRepository;

    @Override
    public HealthInfoDTO addHealthInfo(Long driverId, HealthInfoRequest healthInfoRequest) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        if (healthInfoRequest.getSleepinessLevel() < 0 && healthInfoRequest.getConcentrationLevel() < 0
                && healthInfoRequest.getStressLevel() < 0) {
            throw new UserException(UserException.UserExceptionProfile.INVALID_HEALTH_DATA);
        }

        return new HealthInfoDTO(healthInfoRepository.save(
                HealthInfo.builder()
                        .time(LocalDateTime.now(Clock.systemUTC()))
                        .driver(driver)
                        .stressLevel(healthInfoRequest.getStressLevel())
                        .concentrationLevel(healthInfoRequest.getConcentrationLevel())
                        .sleepinessLevel(healthInfoRequest.getSleepinessLevel())
                        .build()
        ));
    }

    @Override
    public HealthInfoDTO getCurrentHealthInfo(Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        var healthInfo = healthInfoRepository.findFirstByDriverOrderByTimeDesc(driver).orElse(null);

        if (healthInfo == null) {
            return null;
        }

        return new HealthInfoDTO(healthInfo);

    }
}
