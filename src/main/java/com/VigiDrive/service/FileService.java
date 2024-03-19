package com.VigiDrive.service;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface FileService {

    void generateGeneralReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException, SituationException;

    void generateHealthReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException;

    void generateSituationReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException;

    void generateWeekDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException;

    void generateMonthDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException;

    void generateDatabaseZipDump(HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException;

    boolean importDatabase(String sql) throws SQLException, ClassNotFoundException;
}
