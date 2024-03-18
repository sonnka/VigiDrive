package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.response.GeneralReportTemplate;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.service.HealthInfoService;
import com.VigiDrive.service.PDFService;
import com.VigiDrive.service.SituationService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PDFServiceImpl implements PDFService {

    private final Font font12 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
    private final Font font14 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.FontStyle.BOLD.ordinal(),
            BaseColor.BLACK);
    private final HealthInfoService healthInfoService;
    private final SituationService situationService;
    private final DriverRepository driverRepository;
    private final ManagerRepository managerRepository;
    private Driver driver;

    public PDFServiceImpl(HealthInfoService healthInfoService, SituationService situationService,
                          DriverRepository driverRepository, ManagerRepository managerRepository) {
        this.healthInfoService = healthInfoService;
        this.situationService = situationService;
        this.driverRepository = driverRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public void generateGeneralReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws IOException, DocumentException, UserException, SituationException {
        configureResponse(response, "_general_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, driverId, managerId, "General report");
        List<HealthInfoDTO> healthInfo = healthInfoService.getMonthHealthInfo(driver);
        List<SituationDTO> situations = situationService.getMonthSituations(driver);

        fillGeneralData(document, healthInfo, situations);

        document.close();
    }

    @Override
    public void generateHealthReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException {
        configureResponse(response, "_health_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, driverId, managerId, "Health report");

        List<HealthInfoDTO> healthInfo = healthInfoService.getWeekHealthInfo(driver);

        fillHealthData(document, healthInfo);

        document.close();
    }

    @Override
    public void generateSituationReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws DocumentException, IOException, UserException {
        configureResponse(response, "_situation_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, driverId, managerId, "Situation report");

        List<SituationDTO> situations = situationService.getWeekSituations(email, driverId);

        fillSituationData(document, situations);

        document.close();
    }

    private void configureDocument(Document document, String email, Long driverId, Long managerId, String reportType)
            throws DocumentException, UserException {
        var today = LocalDate.now().atTime(0, 0, 0);
        var startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);

        var reportName = reportType + "\n" + startOfWeek.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " - " + today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        var manager = managerRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.USER_NOT_FOUND)
        );

        if (!Objects.equals(manager.getId(), managerId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        driver = driverRepository.findById(driverId).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.DRIVER_NOT_FOUND)
        );

        setTitleOfDocument(document, reportName, driver, manager);
    }

    private void configureResponse(HttpServletResponse response, String reportType) {
        response.setContentType("application/pdf");
        var fileName = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss")) + reportType + ".pdf";
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

    private void fillGeneralData(Document document, List<HealthInfoDTO> info, List<SituationDTO> situations)
            throws SituationException, DocumentException {

        var data = getGeneralData(info, situations);

        PdfPTable table = new PdfPTable(4);

        Stream.of("Date", "General health status", "Amount of situations", "Most frequent situation")
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

        for (Map.Entry<LocalDate, GeneralReportTemplate> genInfo : data) {
            var dateCell = table.addCell(new PdfPCell(new Phrase(genInfo.getKey()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), font12)));

            var healthCell = table.addCell(new PdfPCell(new Phrase(
                    genInfo.getValue().getGeneralHealthStatus(),
                    font12)));

            var amountCell = table.addCell(new PdfPCell(new Phrase(
                    genInfo.getValue().getAmountOfSituations(),
                    font12)));

            var freqCell = table.addCell(new PdfPCell(new Phrase(
                    genInfo.getValue().getMostFrequentSituation().toLowerCase().replace("_", " "),
                    font12)));

            for (PdfPCell cell : List.of(dateCell, healthCell, amountCell, freqCell)) {
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(1);
                cell.setMinimumHeight(40f);
            }
        }
        table.setWidthPercentage(100f);
        document.add(table);
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

    private void fillSituationData(Document document, List<SituationDTO> situations) throws DocumentException {
        PdfPTable table = new PdfPTable(5);

        Stream.of("Start", "End", "Type", "Description", "Video")
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

        for (SituationDTO situation : situations) {
            var startCell = table.addCell(new PdfPCell(new Phrase(situation.getStart()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")), font12)));

            var endCell = table.addCell(new PdfPCell(new Phrase(situation.getEnd()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss")), font12)));

            var typeCell = table.addCell(new PdfPCell(new Phrase(
                    situation.getType().toString().toLowerCase().replace("_", " "),
                    font12)));

            var descCell = table.addCell(new PdfPCell(
                    new Phrase(situation.getDescription(), font12)));

            var videoCell = table.addCell(new PdfPCell(new Phrase(situation.getVideo(), font12)));

            for (PdfPCell cell : List.of(startCell, endCell, typeCell, descCell, videoCell)) {
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(1);
                cell.setMinimumHeight(50f);
            }
        }
        table.setWidthPercentage(100f);
        document.add(table);
    }

    private List<Map.Entry<LocalDate, GeneralReportTemplate>> getGeneralData(List<HealthInfoDTO> info,
                                                                             List<SituationDTO> situations)
            throws SituationException {
        Map<LocalDate, List<HealthInfoDTO>> resultHealth = info.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getTime().toLocalDate())
                );

        Map<LocalDate, List<SituationDTO>> resultSituations = situations.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getStart().toLocalDate())
                );

        Map<LocalDate, GeneralReportTemplate> result = new HashMap<>();

        for (LocalDate date : resultHealth.keySet()) {
            var currentInfo = resultHealth.get(date);

            var optionalGeneralHealthStatus = currentInfo.stream()
                    .mapToDouble(HealthInfoDTO::getGeneralStatus)
                    .average().orElse(0);

            var generalHealthStatus = BigDecimal.valueOf(optionalGeneralHealthStatus)
                    .setScale(2, RoundingMode.HALF_UP)
                    .toString();

            List<SituationDTO> currentSituations = resultSituations.get(date);

            String mostFrequentSituation = "-";
            String amount = "-";

            if (currentSituations != null) {
                amount = String.valueOf(currentSituations.size());

                mostFrequentSituation = calculateMostFrequentSituation(currentSituations);
            }

            result.put(date, GeneralReportTemplate.builder()
                    .generalHealthStatus(generalHealthStatus)
                    .amountOfSituations(amount)
                    .mostFrequentSituation(mostFrequentSituation)
                    .build()
            );
        }

        for (LocalDate date : resultSituations.keySet()) {

            if (!result.containsKey(date)) {
                List<SituationDTO> currentSituations = resultSituations.get(date);

                String mostFrequentSituation = "-";

                if (!currentSituations.isEmpty()) {
                    mostFrequentSituation = calculateMostFrequentSituation(currentSituations);
                }

                result.put(date, GeneralReportTemplate.builder()
                        .generalHealthStatus("-")
                        .amountOfSituations(String.valueOf(currentSituations.size()))
                        .mostFrequentSituation(mostFrequentSituation)
                        .build()
                );
            }
        }

        return result.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                .toList();
    }


    private String calculateMostFrequentSituation(List<SituationDTO> situations) throws SituationException {
        return situations.stream()
                .max(Comparator.comparing(SituationDTO::getType))
                .orElseThrow(() ->
                        new SituationException(SituationException.SituationExceptionProfile.SOMETHING_WRONG))
                .getType().toString();
    }
}
