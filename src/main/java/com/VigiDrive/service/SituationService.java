package com.VigiDrive.service;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SituationService {

    List<SituationDTO> getSituations(Authentication auth, Long driverId) throws UserException;

    SituationDTO getSituation(Authentication auth, Long driverId, Long situationId)
            throws UserException, SituationException;

    SituationDTO addSituation(Authentication auth, Long driverId, SituationRequest situation)
            throws UserException, SituationException;
}
