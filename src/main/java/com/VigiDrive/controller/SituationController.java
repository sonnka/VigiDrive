package com.VigiDrive.controller;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.service.SituationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SituationController {

    private SituationService situationService;

    @GetMapping("/drivers/{driver-id}/situations")
    public List<SituationDTO> getSituations(Authentication auth,
                                            @PathVariable("driver-id") Long driverId) throws UserException {
        return situationService.getSituations(auth, driverId);
    }

    @GetMapping("/drivers/{driver-id}/situations/{situation-id}")
    public SituationDTO getSituation(Authentication auth,
                                     @PathVariable("driver-id") Long driverId,
                                     @PathVariable("situation-id") Long situationId)
            throws SituationException, UserException {
        return situationService.getSituation(auth, driverId, situationId);
    }

    @PostMapping("/drivers/{driver-id}/situations")
    public SituationDTO addSituation(Authentication auth,
                                     @PathVariable("driver-id") Long driverId,
                                     @RequestBody @Valid SituationRequest situation)
            throws SituationException, UserException {
        return situationService.addSituation(auth, driverId, situation);
    }
}
