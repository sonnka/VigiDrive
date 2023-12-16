package com.VigiDrive.controller;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.service.HealthInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class HealthInfoController {

    private HealthInfoService healthInfoService;

    @PostMapping("/drivers/{driver-id}/health-info")
    public HealthInfoDTO addHealthInfo(Authentication auth,
                                       @PathVariable("driver-id") Long driverId,
                                       @RequestBody @Valid HealthInfoRequest healthInfoRequest) throws UserException {
        return healthInfoService.addHealthInfo(auth, driverId, healthInfoRequest);
    }

    @GetMapping("/drivers/{driver-id}/health-info")
    public HealthInfoDTO getCurrentHealthInfo(Authentication auth,
                                              @PathVariable("driver-id") Long driverId) throws UserException {
        return healthInfoService.getCurrentHealthInfo(auth, driverId);
    }
}
