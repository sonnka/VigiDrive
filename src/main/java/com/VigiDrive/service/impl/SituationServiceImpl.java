package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Situation;
import com.VigiDrive.model.enums.SituationType;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.SituationRepository;
import com.VigiDrive.service.SituationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class SituationServiceImpl implements SituationService {

    private SituationRepository situationRepository;
    private DriverRepository driverRepository;

    @Override
    public List<SituationDTO> getSituations(Authentication auth, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        return situationRepository.findAllByDriver(driver).stream()
                .map(SituationDTO::new)
                .toList();
    }

    @Override
    public List<SituationDTO> getWeekSituations(Authentication auth, Long driverId) throws UserException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        return situationRepository.findAllByDriverAndStartGreaterThan(driver, getStartOfCurrentWeek())
                .stream()
                .map(SituationDTO::new)
                .toList();
    }

    private LocalDateTime getStartOfCurrentWeek() {
        var today = LocalDate.now();
        return LocalDate.now()
                .minusDays(today.getDayOfWeek().getValue() - 1L)
                .atTime(0, 0, 0);
    }

    @Override
    public SituationDTO getSituation(Authentication auth, Long driverId, Long situationId)
            throws UserException, SituationException {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        var situation = situationRepository.findById(situationId)
                .orElseThrow(() -> new SituationException(SituationException.SituationExceptionProfile.SITUATION_NOT_FOUND));

        if (!Objects.equals(situation.getDriver().getId(), driverId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        return new SituationDTO(situation);
    }

    @Override
    public SituationDTO addSituation(Authentication auth, Long driverId, SituationRequest situation)
            throws UserException, SituationException {
        String videoUrlRegex = "^(https?://)?(www\\.)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/\\S*)?$";

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND));

        LocalDateTime start, end;

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
}
