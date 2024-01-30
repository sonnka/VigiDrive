package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.HealthInfo;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;
import com.VigiDrive.model.response.StatisticElement;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.HealthInfoRepository;
import com.VigiDrive.service.HealthInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class HealthInfoServiceImpl implements HealthInfoService {

    private HealthInfoRepository healthInfoRepository;
    private DriverRepository driverRepository;

    @Override
    public HealthInfoDTO addHealthInfo(Authentication auth, Long driverId, HealthInfoRequest healthInfoRequest)
            throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));
        log.error(healthInfoRequest.toString());
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
    public HealthInfoDTO getCurrentHealthInfo(Authentication auth, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        var healthInfo = healthInfoRepository.findFirstByDriverOrderByTimeDesc(driver).orElse(null);

        if (healthInfo == null) {
            return null;
        }

        return new HealthInfoDTO(healthInfo);

    }

    @Override
    public HealthStatistics getWeekHealthStatistics(Authentication auth, Long driverId)
            throws UserException, HealthException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);

        List<HealthInfoDTO> healthInfo = healthInfoRepository.findAllByDriverAndTimeAfter(driver, startOfWeek)
                .stream()
                .map(HealthInfoDTO::new)
                .toList();

        Map<Integer, List<HealthInfoDTO>> resultMap = healthInfo.stream()
                .collect(Collectors.groupingBy(
                        info -> info.getTime().toLocalDate().getDayOfWeek().getValue()
                ));

        double average = BigDecimal.valueOf(
                        healthInfo
                                .stream()
                                .mapToDouble(HealthInfoDTO::getGeneralStatus)
                                .average()
                                .orElse(0))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        Map<Integer, Double> frequency = getFrequency(resultMap);

        List<StatisticElement> statistics = new ArrayList<>();

        frequency.forEach((key, value) -> statistics.add(new StatisticElement(key, value)));

        return HealthStatistics.builder()
                .generalLevelForPeriod(average)
                .bestPeriod(getBestPeriod(frequency))
                .worstPeriod(getWorstPeriod(frequency))
                .statistics(statistics)
                .build();
    }

    private int getBestPeriod(Map<Integer, Double> frequency)
            throws HealthException {

        return frequency.entrySet()
                .stream().min(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .orElseThrow(() ->
                        new HealthException(HealthException.HealthExceptionProfile.SOMETHING_WRONG))
                .getKey();
    }

    private int getWorstPeriod(Map<Integer, Double> frequency)
            throws HealthException {

        return frequency.entrySet()
                .stream().max(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .orElseThrow(() ->
                        new HealthException(HealthException.HealthExceptionProfile.SOMETHING_WRONG))
                .getKey();
    }

    private Map<Integer, Double> getFrequency(Map<Integer, List<HealthInfoDTO>> resultMap) {
        Map<Integer, Double> frequency = new HashMap<>();

        for (Integer period : resultMap.keySet()) {
            var average = resultMap.get(period)
                    .stream()
                    .mapToDouble(HealthInfoDTO::getGeneralStatus)
                    .average()
                    .orElse(0);

            frequency.put(period,
                    BigDecimal.valueOf(average)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue());
        }

        return frequency;
    }
}
