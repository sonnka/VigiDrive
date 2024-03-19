package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.HealthInfo;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;
import com.VigiDrive.model.response.StatisticElement;
import com.VigiDrive.repository.HealthInfoRepository;
import com.VigiDrive.service.HealthInfoService;
import com.VigiDrive.util.AuthUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private AuthUtil authUtil;

    @Override
    public HealthInfoDTO addHealthInfo(String email, Long driverId, HealthInfoRequest healthInfoRequest)
            throws UserException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

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
    public HealthInfoDTO getCurrentHealthInfo(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var healthInfo = healthInfoRepository.findFirstByDriverOrderByTimeDesc(driver).orElse(null);

        if (healthInfo == null) {
            return null;
        }

        return new HealthInfoDTO(healthInfo);
    }

    @Override
    public List<HealthInfoDTO> getWeekHealthInfo(Driver driver) {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        List<HealthInfo> info = healthInfoRepository.findAllByDriverAndTimeAfter(driver, startOfWeek);

        return info.stream()
                .map(HealthInfoDTO::new)
                .sorted(Comparator.comparing(HealthInfoDTO::getTime))
                .toList();
    }

    @Override
    public List<HealthInfoDTO> getMonthHealthInfo(Driver driver) {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

        List<HealthInfo> info = healthInfoRepository.findAllByDriverAndTimeAfter(driver, startOfMonth);

        return info.stream()
                .map(HealthInfoDTO::new)
                .sorted(Comparator.comparing(HealthInfoDTO::getTime))
                .toList();
    }

    @Override
    public HealthStatistics getWeekHealthStatistics(String email, Long driverId)
            throws UserException, HealthException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        List<HealthInfoDTO> healthInfo = healthInfoRepository.findAllByDriverAndTimeAfter(driver, startOfWeek)
                .stream()
                .map(HealthInfoDTO::new)
                .toList();

        Map<Integer, List<HealthInfoDTO>> resultMap = healthInfo.stream()
                .collect(Collectors.groupingBy(
                        info -> info.getTime().toLocalDate().getDayOfWeek().getValue()
                ));

        return getHealthStatistics(healthInfo, resultMap);
    }

    @Override
    public HealthStatistics getMonthHealthStatistics(String email, Long driverId)
            throws UserException, HealthException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

        List<HealthInfoDTO> healthInfo = healthInfoRepository.findAllByDriverAndTimeAfter(driver, startOfMonth)
                .stream()
                .map(HealthInfoDTO::new)
                .toList();

        Map<Integer, List<HealthInfoDTO>> resultMap = healthInfo.stream()
                .collect(Collectors.groupingBy(
                        info -> info.getTime().toLocalDate().getDayOfMonth()
                ));

        return getHealthStatistics(healthInfo, resultMap);
    }


    @Override
    public HealthStatistics getYearHealthStatistics(String email, Long driverId)
            throws UserException, HealthException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfYear = today.minusMonths(today.getMonthValue() - 1L).minusDays(today.getDayOfMonth() - 1L);

        List<HealthInfoDTO> healthInfo = healthInfoRepository.findAllByDriverAndTimeAfter(driver, startOfYear)
                .stream()
                .map(HealthInfoDTO::new)
                .toList();

        Map<Integer, List<HealthInfoDTO>> resultMap = healthInfo.stream()
                .collect(Collectors.groupingBy(
                        info -> info.getTime().toLocalDate().getMonth().getValue()
                ));

        return getHealthStatistics(healthInfo, resultMap);
    }


    private int getBestPeriod(Map<Integer, Double> frequency)
            throws HealthException {
        if (frequency == null || frequency.isEmpty()) {
            return 0;
        }
        return frequency.entrySet()
                .stream().min(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .orElseThrow(() ->
                        new HealthException(HealthException.HealthExceptionProfile.SOMETHING_WRONG))
                .getKey();
    }

    private int getWorstPeriod(Map<Integer, Double> frequency)
            throws HealthException {
        if (frequency == null || frequency.isEmpty()) {
            return 0;
        }
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

    private HealthStatistics getHealthStatistics(List<HealthInfoDTO> healthInfo,
                                                 Map<Integer, List<HealthInfoDTO>> resultMap) throws HealthException {
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
}
