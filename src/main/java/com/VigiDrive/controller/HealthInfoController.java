package com.VigiDrive.controller;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;
import com.VigiDrive.service.HealthInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class HealthInfoController {

    private HealthInfoService healthInfoService;

    @PostMapping("/drivers/{driver-id}/health-info")
    public HealthInfoDTO addHealthInfo(Authentication auth,
                                       @PathVariable("driver-id") Long driverId,
                                       @RequestBody @Valid HealthInfoRequest healthInfoRequest) throws UserException {
        return healthInfoService.addHealthInfo(auth.getName(), driverId, healthInfoRequest);
    }

    @GetMapping("/drivers/{driver-id}/health-info")
    public HealthInfoDTO getCurrentHealthInfo(Authentication auth,
                                              @PathVariable("driver-id") Long driverId) throws UserException {
        return healthInfoService.getCurrentHealthInfo(auth.getName(), driverId);
    }

    @GetMapping("/drivers/{driver-id}/health-info/statistics/week")
    public HealthStatistics getWeekHealthStatistics(Authentication auth,
                                                    @PathVariable("driver-id") Long driverId)
            throws HealthException, UserException {
        return healthInfoService.getWeekHealthStatistics(auth.getName(), driverId);
    }

    @GetMapping("/drivers/{driver-id}/health-info/statistics/month")
    public HealthStatistics getMonthHealthStatistics(Authentication auth,
                                                     @PathVariable("driver-id") Long driverId)
            throws HealthException, UserException {
        return healthInfoService.getMonthHealthStatistics(auth.getName(), driverId);
    }

    @GetMapping("/drivers/{driver-id}/health-info/statistics/year")
    public HealthStatistics getYearHealthStatistics(Authentication auth,
                                                    @PathVariable("driver-id") Long driverId)
            throws HealthException, UserException {
        return healthInfoService.getYearHealthStatistics(auth.getName(), driverId);
    }
}
