package com.VigiDrive.service;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.model.response.SituationStatistics;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SituationService {

    List<SituationDTO> getSituations(Authentication auth, Long driverId) throws UserException;

    List<SituationDTO> getWeekSituations(Authentication auth, Long driverId) throws UserException;

    SituationDTO getSituation(Authentication auth, Long driverId, Long situationId)
            throws UserException, SituationException;

    SituationDTO addSituation(Authentication auth, Long driverId, SituationRequest situation)
            throws UserException, SituationException;

    SituationStatistics getWeekStatistic(Authentication auth, Long driverId) throws UserException, SituationException;

    SituationStatistics getMonthStatistic(Authentication auth, Long driverId) throws UserException, SituationException;

    SituationStatistics getYearStatistic(Authentication auth, Long driverId) throws UserException, SituationException;
}
