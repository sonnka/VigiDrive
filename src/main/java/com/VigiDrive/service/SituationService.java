package com.VigiDrive.service;

import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;

import java.util.List;

public interface SituationService {

    List<SituationDTO> getSituations(Long driverId);

    SituationDTO getSituation(Long driverId, Long situationId);

    SituationDTO addSituation(Long driverId, SituationRequest situation);
}
