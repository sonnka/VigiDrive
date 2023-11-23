package com.VigiDrive.service;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;

import java.util.List;

public interface SituationService {

    List<SituationDTO> getSituations(Long driverId) throws UserException;

    SituationDTO getSituation(Long driverId, Long situationId) throws UserException, SituationException;

    SituationDTO addSituation(Long driverId, SituationRequest situation) throws UserException, SituationException;
}
