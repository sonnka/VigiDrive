package com.VigiDrive.service;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.model.response.SituationStatistics;

import java.util.List;

public interface SituationService {

    List<SituationDTO> getSituations(String email, Long driverId) throws UserException;

    List<SituationDTO> getWeekSituations(String email, Long driverId) throws UserException;

    List<SituationDTO> getMonthSituations(Driver driver);

    SituationDTO getSituation(String email, Long driverId, Long situationId)
            throws UserException, SituationException;

    SituationDTO addSituation(String email, Long driverId, SituationRequest situation)
            throws UserException, SituationException;

    SituationStatistics getWeekStatistic(String email, Long driverId) throws UserException, SituationException;

    SituationStatistics getMonthStatistic(String email, Long driverId) throws UserException, SituationException;

    SituationStatistics getYearStatistic(String email, Long driverId) throws UserException, SituationException;
}
