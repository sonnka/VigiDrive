package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Situation;
import com.VigiDrive.model.enums.SituationType;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.model.response.SituationStatistics;
import com.VigiDrive.model.response.StatisticElement;
import com.VigiDrive.repository.SituationRepository;
import com.VigiDrive.service.SituationService;
import com.VigiDrive.util.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SituationServiceImpl implements SituationService {

    private SituationRepository situationRepository;
    private AuthUtil authUtil;

    @Override
    public List<SituationDTO> getSituations(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        return situationRepository.findAllByDriver(driver).stream()
                .map(SituationDTO::new)
                .toList();
    }

    @Override
    public List<SituationDTO> getWeekSituations(String email, Long driverId) throws UserException {
        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        return situationRepository.findAllByDriverAndStartGreaterThanOrderByStartAsc(driver, getStartOfCurrentWeek())
                .stream()
                .map(SituationDTO::new)
                .toList();
    }

    @Override
    public List<SituationDTO> getMonthSituations(Driver driver) {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

        return situationRepository.findAllByDriverAndStartGreaterThanOrderByStartAsc(driver, startOfMonth)
                .stream().map(SituationDTO::new).toList();
    }

    private LocalDateTime getStartOfCurrentWeek() {
        var today = LocalDate.now();
        return LocalDate.now()
                .minusDays(today.getDayOfWeek().getValue() - 1L)
                .atTime(0, 0, 0);
    }

    @Override
    public SituationDTO getSituation(String email, Long driverId, Long situationId)
            throws UserException, SituationException {
        authUtil.findDriverByEmailAndId(email, driverId);

        var situation = situationRepository.findById(situationId)
                .orElseThrow(() -> new SituationException(SituationException.SituationExceptionProfile.SITUATION_NOT_FOUND));

        if (!Objects.equals(situation.getDriver().getId(), driverId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        return new SituationDTO(situation);
    }

    @Override
    public SituationDTO addSituation(String email, Long driverId, SituationRequest situation)
            throws UserException, SituationException {
        String videoUrlRegex = "^(https?://)?(www\\.)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/\\S*)?$";

        var driver = authUtil.findDriverByEmailAndId(email, driverId);

        LocalDateTime start;
        LocalDateTime end;

        try {
            start = LocalDateTime.parse(situation.getStart(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            end = LocalDateTime.parse(situation.getEnd(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new SituationException(SituationException.SituationExceptionProfile.INVALID_TIME);
        }

        SituationType type = SituationType.valueOf(situation.getType().toUpperCase());

        if (situation.getVideo() != null) {
            Pattern pattern = Pattern.compile(videoUrlRegex);
            Matcher matcher = pattern.matcher(situation.getVideo());

            if (!matcher.matches()) {
                throw new SituationException(SituationException.SituationExceptionProfile.INVALID_VIDEO_URL);
            }
        }
        return new SituationDTO(situationRepository.save(
                Situation.builder()
                        .start(start)
                        .end(end)
                        .type(type)
                        .description(situation.getDescription())
                        .video(situation.getVideo())
                        .driver(driver)
                        .build())
        );
    }

    @Override
    public SituationStatistics getWeekStatistic(String email, Long driverId)
            throws UserException, SituationException {

        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        List<SituationDTO> situations = situationRepository
                .findAllByDriverAndStartGreaterThanOrderByStartAsc(driver, startOfWeek)
                .stream().map(SituationDTO::new).toList();

        Map<Integer, List<SituationDTO>> resultMap = situations.stream().collect(Collectors.groupingBy(
                u -> u.getStart().getDayOfWeek().getValue())
        );

        return getSituationStatistics(resultMap, situations);
    }

    @Override
    public SituationStatistics getMonthStatistic(String email, Long driverId)
            throws UserException, SituationException {

        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

        List<SituationDTO> situations = situationRepository
                .findAllByDriverAndStartGreaterThanOrderByStartAsc(driver, startOfMonth)
                .stream().map(SituationDTO::new).toList();

        Map<Integer, List<SituationDTO>> resultMap = situations.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getStart().getDayOfMonth())
                );

        return getSituationStatistics(resultMap, situations);
    }

    @Override
    public SituationStatistics getYearStatistic(String email, Long driverId) throws UserException, SituationException {

        var driver = authUtil.findDriverByEmailAndIdAndCheckByManager(email, driverId);

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfYear = today.minusMonths(today.getMonthValue() - 1L).minusDays(today.getDayOfMonth() - 1L);

        List<SituationDTO> situations = situationRepository
                .findAllByDriverAndStartGreaterThanOrderByStartAsc(driver, startOfYear)
                .stream().map(SituationDTO::new).toList();

        Map<Integer, List<SituationDTO>> resultMap = situations.stream()
                .collect(Collectors.groupingBy(
                        u -> u.getStart().getMonth().getValue())
                );

        return getSituationStatistics(resultMap, situations);
    }

    private SituationStatistics getSituationStatistics(Map<Integer, List<SituationDTO>> map,
                                                       List<SituationDTO> situations) throws SituationException {
        List<StatisticElement> result = new ArrayList<>();

        map.forEach((key, value) -> result.add(new StatisticElement(key, (double) value.size())));

        if (result.isEmpty()) {
            return new SituationStatistics();
        }

        var mostFrequentPeriod = result.stream()
                .max(Comparator.comparingDouble(StatisticElement::getAmount))
                .orElseThrow(() ->
                        new SituationException(SituationException.SituationExceptionProfile.SOMETHING_WRONG))
                .getPeriod();

        return SituationStatistics.builder()
                .amountOfSituations(map.values().stream().mapToInt(List::size).sum())
                .mostFrequentSituation(getMostFrequentSituation(situations))
                .mostFrequentPeriod(mostFrequentPeriod)
                .statistics(result)
                .build();
    }

    private SituationType getMostFrequentSituation(List<SituationDTO> list) throws SituationException {
        Map<SituationType, Integer> frequency = new EnumMap<>(SituationType.class);

        for (SituationDTO situation : list) {
            frequency.put(
                    situation.getType(),
                    list.stream().filter(s -> s.getType().equals(situation.getType())).toList().size()
            );
        }

        if (frequency.isEmpty()) {
            return null;
        }

        return frequency.entrySet()
                .stream().min(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .orElseThrow(() ->
                        new SituationException(SituationException.SituationExceptionProfile.SOMETHING_WRONG))
                .getKey();
    }
}