package com.VigiDrive.controller;

import com.VigiDrive.exceptions.HealthException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.request.HealthInfoRequest;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.HealthStatistics;
import com.VigiDrive.service.HealthInfoService;
import com.VigiDrive.service.PDFService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class HealthInfoController {

    private HealthInfoService healthInfoService;
    private PDFService pdfService;

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

    @GetMapping("/drivers/{driver-id}/health-info/statistics/week")
    public HealthStatistics getWeekHealthStatistics(Authentication auth,
                                                    @PathVariable("driver-id") Long driverId)
            throws HealthException, UserException {
        return healthInfoService.getWeekHealthStatistics(auth, driverId);
    }

    @GetMapping("/drivers/{driver-id}/health-info/statistics/month")
    public HealthStatistics getMonthHealthStatistics(Authentication auth,
                                                     @PathVariable("driver-id") Long driverId)
            throws HealthException, UserException {
        return healthInfoService.getMonthHealthStatistics(auth, driverId);
    }

    @GetMapping("/drivers/{driver-id}/health-info/statistics/year")
    public HealthStatistics getYearHealthStatistics(Authentication auth,
                                                    @PathVariable("driver-id") Long driverId)
            throws HealthException, UserException {
        return healthInfoService.getYearHealthStatistics(auth, driverId);
    }

    @GetMapping("/managers/{manager-id}/drivers/{driver-id}/health-report")
    public void exportHealthReport(Authentication auth,
                                   HttpServletResponse response,
                                   @PathVariable("manager-id") Long managerId,
                                   @PathVariable("driver-id") Long driverId)
            throws DocumentException, IOException, UserException {
        pdfService.generateReport(auth.getName(), managerId, driverId, response);
    }
}
