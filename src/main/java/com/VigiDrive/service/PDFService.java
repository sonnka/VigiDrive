package com.VigiDrive.service;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PDFService {

    void generateGeneralReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException, SituationException;

    void generateHealthReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException;

    void generateSituationReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException;
}
