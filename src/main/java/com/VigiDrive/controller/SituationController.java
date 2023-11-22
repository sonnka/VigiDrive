package com.VigiDrive.controller;

import com.VigiDrive.model.request.SituationRequest;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.service.SituationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SituationController {

    private SituationService situationService;

    @GetMapping("/drivers/{driver-id}/situations")
    public List<SituationDTO> getSituations(@PathVariable("driver-id") Long driverId) {
        return situationService.getSituations(driverId);
    }

    @GetMapping("/drivers/{driver-id}/situations/{situation-id}")
    public SituationDTO getSituation(@PathVariable("driver-id") Long driverId,
                                     @PathVariable("situation-id") Long situationId) {
        return situationService.getSituation(driverId, situationId);
    }

    @PostMapping("/drivers/{driver-id}/situations")
    public SituationDTO addSituation(@PathVariable("driver-id") Long driverId,
                                     @RequestBody @Valid SituationRequest situation) {
        return situationService.addSituation(driverId, situation);
    }
}
