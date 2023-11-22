package com.VigiDrive.service.impl;

import com.VigiDrive.model.entity.Situation;
import com.VigiDrive.model.enums.SituationType;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.SituationRepository;
import com.VigiDrive.service.SituationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<SituationDTO> getSituations(Long driverId) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        return situationRepository.findAllByDriver(driver).stream()
                .map(SituationDTO::new)
                .toList();
    }

    @Override
    public SituationDTO getSituation(Long driverId, Long situationId) {
        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        var situation = situationRepository.findById(situationId)
                .orElseThrow(() -> new RuntimeException("Situation not found!"));

        if (!Objects.equals(situation.getDriver().getId(), driverId)) {
            throw new RuntimeException("Permission denied");
        }

        return new SituationDTO(situation);
    }

    @Override
    public SituationDTO addSituation(Long driverId, SituationRequest situation) {
        String videoUrlRegex = "^(https?://)?(www\\.)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(/\\S*)?$";

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found!"));

        LocalDateTime start = null;
        LocalDateTime end = null;

        try {
            start = LocalDateTime.parse(situation.getStart(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            end = LocalDateTime.parse(situation.getEnd(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Time is invalid!");
        }

        SituationType type = SituationType.valueOf(situation.getType().toUpperCase());

        if (situation.getVideo() != null) {
            Pattern pattern = Pattern.compile(videoUrlRegex);
            Matcher matcher = pattern.matcher(situation.getVideo());

            if (!matcher.matches()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Video url is invalid!");
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
