package com.VigiDrive.controller;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.service.PDFService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ReportController {

    private PDFService pdfService;

    @GetMapping("/managers/{manager-id}/drivers/{driver-id}/general-report")
    public void exportGeneralReport(Authentication auth,
                                    HttpServletResponse response,
                                    @PathVariable("manager-id") Long managerId,
                                    @PathVariable("driver-id") Long driverId)
            throws DocumentException, IOException, UserException, SituationException {
        pdfService.generateGeneralReport(auth.getName(), managerId, driverId, response);
    }

    @GetMapping("/managers/{manager-id}/drivers/{driver-id}/health-report")
    public void exportHealthReport(Authentication auth,
                                   HttpServletResponse response,
                                   @PathVariable("manager-id") Long managerId,
                                   @PathVariable("driver-id") Long driverId)
            throws DocumentException, IOException, UserException {
        pdfService.generateHealthReport(auth.getName(), managerId, driverId, response);
    }

    @GetMapping("/managers/{manager-id}/drivers/{driver-id}/situation-report")
    public void exportSituationReport(Authentication auth,
                                      HttpServletResponse response,
                                      @PathVariable("manager-id") Long managerId,
                                      @PathVariable("driver-id") Long driverId)
            throws DocumentException, IOException, UserException {
        pdfService.generateSituationReport(auth.getName(), managerId, driverId, response);
    }

    @GetMapping("/admins/{admin-id}/db-report/week")
    public void exportWeekDatabaseReport(Authentication auth,
                                         HttpServletResponse response,
                                         @PathVariable("admin-id") Long adminId)
            throws DocumentException, IOException, UserException {
        pdfService.generateWeekDatabaseReport(auth.getName(), adminId, response);
    }

    @GetMapping("/admins/{admin-id}/db-report/month")
    public void exportMonthDatabaseReport(Authentication auth,
                                          HttpServletResponse response,
                                          @PathVariable("admin-id") Long adminId)
            throws DocumentException, IOException, UserException {
        pdfService.generateMonthDatabaseReport(auth.getName(), adminId, response);
    }
}
