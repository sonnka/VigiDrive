package com.VigiDrive.controller;

import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.service.HealthInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class HealthInfoController {

    private HealthInfoService healthInfoService;

    @PostMapping("/drivers/{driver-id}/health-info")
    public HealthInfoDTO addHealthInfo(@PathVariable("driver-id") Long driverId,
                                       @RequestBody @Valid HealthInfoRequest healthInfoRequest) {
        return healthInfoService.addHealthInfo(driverId, healthInfoRequest);
    }

    @GetMapping("/drivers/{driver-id}/health-info")
    public HealthInfoDTO getCurrentHealthInfo(@PathVariable("driver-id") Long driverId) {
        return healthInfoService.getCurrentHealthInfo(driverId);
    }
}
