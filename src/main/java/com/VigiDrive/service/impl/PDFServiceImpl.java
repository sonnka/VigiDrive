package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.service.HealthInfoService;
import com.VigiDrive.service.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class PDFServiceImpl implements PDFService {

    private final Font font12 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
    private final Font font14 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.FontStyle.BOLD.ordinal(),
            BaseColor.BLACK);

    private final HealthInfoService healthInfoService;
    private final DriverRepository driverRepository;
    private final ManagerRepository managerRepository;

    public PDFServiceImpl(HealthInfoService healthInfoService, DriverRepository driverRepository,
                          ManagerRepository managerRepository) {
        this.healthInfoService = healthInfoService;
        this.driverRepository = driverRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public void generateReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException {
        configureResponse(response);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        var reportName = "Health report\n" + startOfWeek.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " - " + today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        var manager = managerRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.USER_NOT_FOUND)
        );

        if (!Objects.equals(manager.getId(), managerId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        var driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND)
        );

        setTitleOfDocument(document, reportName, driver, manager);

        List<HealthInfoDTO> healthInfo = healthInfoService.getWeekHealthInfo(driver);

        fillHealthData(document, healthInfo);

        document.close();
    }

    private void configureResponse(HttpServletResponse response) {
        response.setContentType("application/pdf");
        var fileName = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss")) + "_health_report.pdf";
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

    private void setTitleOfDocument(Document document, String reportName, Driver driver, Manager manager)
            throws DocumentException {
        var todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        Paragraph time = new Paragraph(todayDate, font12);

        Paragraph driverText = new Paragraph("Driver:", font12);
        Paragraph driverFullName = new Paragraph(driver.getFirstName() + " " + driver.getLastName(), font12);
        Paragraph driverEmail = new Paragraph(driver.getEmail(), font12);

        Paragraph managerText = new Paragraph("Manager:", font12);
        Paragraph managerFullName = new Paragraph(manager.getFirstName() + " " + manager.getLastName(), font12);
        Paragraph managerEmail = new Paragraph(manager.getEmail(), font12);

        Paragraph title = new Paragraph(reportName, boldFont);

        title.setAlignment(Element.ALIGN_CENTER);
        time.setAlignment(Element.ALIGN_RIGHT);
        driverFullName.setIndentationLeft(30f);
        driverEmail.setIndentationLeft(30f);
        managerFullName.setIndentationLeft(30f);
        managerEmail.setIndentationLeft(30f);

        document.add(time);
        document.add(driverText);
        document.add(driverFullName);
        document.add(driverEmail);
        document.add(managerText);
        document.add(managerFullName);
        document.add(managerEmail);
        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(new Paragraph("\n\n"));
    }

    private void fillHealthData(Document document, List<HealthInfoDTO> info) throws DocumentException {
        PdfPTable table = new PdfPTable(6);

        Stream.of("Date", "Time", "Stress level", "Concentration level", "Sleepiness level", "General status")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(153, 192, 192));
                    header.setBorderWidth(0.5f);
                    header.setPhrase(new Phrase(columnTitle, font14));
                    var headerCell = table.addCell(header);
                    headerCell.setHorizontalAlignment(1);
                    headerCell.setVerticalAlignment(1);
                    headerCell.setFixedHeight(40f);
                });

        for (HealthInfoDTO health : info) {
            var dateCell = table.addCell(new PdfPCell(new Phrase(health.getTime()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), font12)));

            var timeCell = table.addCell(new PdfPCell(new Phrase(health.getTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss")), font12)));

            var stressCell = table.addCell(new PdfPCell(new Phrase(health.getStressLevel().toString(), font12)));

            var conCell = table.addCell(new PdfPCell(
                    new Phrase(health.getConcentrationLevel().toString(), font12)));

            var sleepCell = table.addCell(new PdfPCell(new Phrase(health.getSleepinessLevel().toString(), font12)));

            var genCell = table.addCell(new PdfPCell(new Phrase(health.getGeneralStatus().toString(), font12)));

            if (health.getGeneralStatus() >= 90) {
                genCell.setBackgroundColor(new BaseColor(182, 246, 168));
            } else if (health.getGeneralStatus() <= 60) {
                genCell.setBackgroundColor(new BaseColor(246, 168, 168));
            } else {
                genCell.setBackgroundColor(new BaseColor(238, 246, 168));
            }

            for (PdfPCell cell : List.of(dateCell, timeCell, stressCell, conCell, sleepCell, genCell)) {
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(1);
                cell.setMinimumHeight(40f);
            }
        }
        table.setWidthPercentage(100f);
        document.add(table);
    }

}
