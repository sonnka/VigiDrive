package com.VigiDrive.service.impl;

import com.VigiDrive.exceptions.SituationException;
import com.VigiDrive.exceptions.UserException;
import com.VigiDrive.model.entity.Admin;
import com.VigiDrive.model.entity.Driver;
import com.VigiDrive.model.entity.Manager;
import com.VigiDrive.model.response.DatabaseHistoryDTO;
import com.VigiDrive.model.response.GeneralReportTemplate;
import com.VigiDrive.model.response.HealthInfoDTO;
import com.VigiDrive.model.response.SituationDTO;
import com.VigiDrive.repository.AdminRepository;
import com.VigiDrive.repository.DriverRepository;
import com.VigiDrive.repository.ManagerRepository;
import com.VigiDrive.service.DatabaseHistoryService;
import com.VigiDrive.service.FileService;
import com.VigiDrive.service.HealthInfoService;
import com.VigiDrive.service.SituationService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.smattme.MysqlExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private final Font font12 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
    private final Font font14 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.FontStyle.BOLD.ordinal(),
            BaseColor.BLACK);
    private final LocalDateTime today = LocalDate.now().atTime(0, 0, 0);
    private final LocalDateTime startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);
    private final LocalDateTime startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

    private final HealthInfoService healthInfoService;
    private final SituationService situationService;
    private final DriverRepository driverRepository;
    private final ManagerRepository managerRepository;
    private final AdminRepository adminRepository;
    private final DatabaseHistoryService databaseHistoryService;

    @Value("${database.name}")
    private String dbName;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    private Driver driver;

    public FileServiceImpl(HealthInfoService healthInfoService, SituationService situationService,
                           DriverRepository driverRepository, ManagerRepository managerRepository,
                           AdminRepository adminRepository, DatabaseHistoryService databaseHistoryService) {
        this.healthInfoService = healthInfoService;
        this.situationService = situationService;
        this.driverRepository = driverRepository;
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
        this.databaseHistoryService = databaseHistoryService;
    }

    @Override
    public void generateGeneralReport(String email, Long managerId, Long driverId, HttpServletResponse response)
            throws IOException, DocumentException, UserException, SituationException {
        configureResponse(response, "_general_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, driverId, managerId, "General report", startOfMonth);
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

        configureDocument(document, email, driverId, managerId, "Health report", startOfWeek);

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

        configureDocument(document, email, driverId, managerId, "Situation report", startOfWeek);

        List<SituationDTO> situations = situationService.getWeekSituations(email, driverId);

        fillSituationData(document, situations);

        document.close();
    }

    @Override
    public void generateWeekDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException {
        configureResponse(response, "_week_database_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, adminId, "Week database report", startOfWeek);

        List<DatabaseHistoryDTO> history = databaseHistoryService.getWeekDatabaseHistory();

        fillDatabaseData(document, history);

        document.close();
    }

    @Override
    public void generateMonthDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException {
        configureResponse(response, "_month_database_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, adminId, "Month database report", startOfMonth);

        List<DatabaseHistoryDTO> history = databaseHistoryService.getMonthDatabaseHistory();

        fillDatabaseData(document, history);

        document.close();
    }

    @Override
    public void generateDatabaseZipDump(HttpServletResponse response) throws SQLException, IOException,
            ClassNotFoundException {
        MysqlExportService mysqlExportService = getMysqlExportService();

        File file = mysqlExportService.getGeneratedZipFile();

        response.setContentType("application/zip");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getName();
        response.setHeader(headerKey, headerValue);

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream fileInputStream = new FileInputStream(file);

        IOUtils.copy(fileInputStream, zipOutputStream);

        fileInputStream.close();
        zipOutputStream.closeEntry();

        zipOutputStream.close();

        mysqlExportService.clearTempFiles();
    }

    private MysqlExportService getMysqlExportService() throws IOException, SQLException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, dbName);
        properties.setProperty(MysqlExportService.DB_USERNAME, dbUsername);
        properties.setProperty(MysqlExportService.DB_PASSWORD, dbPassword);
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());
        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, dbUrl);
        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");

        MysqlExportService mysqlExportService = new MysqlExportService(properties);

        mysqlExportService.export();
        return mysqlExportService;
    }

    private void configureDocument(Document document, String email, Long driverId, Long managerId, String reportType,
                                   LocalDateTime startOfPeriod)
            throws DocumentException, UserException {

        var reportName = reportType + "\n" + startOfPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
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

    private void configureDocument(Document document, String email, Long adminId, String reportType,
                                   LocalDateTime startOfPeriod) throws UserException, DocumentException {
        var reportName = reportType + "\n" + startOfPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " - " + today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND)
        );

        if (!Objects.equals(admin.getId(), adminId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        setTitleOfDocument(document, reportName, admin);
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

    private void setTitleOfDocument(Document document, String reportName, Admin admin)
            throws DocumentException {
        var todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        Paragraph time = new Paragraph(todayDate, font12);

        Paragraph adminText = new Paragraph("Admin:", font12);
        Paragraph adminFullName = new Paragraph(admin.getFirstName() + " " + admin.getLastName(), font12);
        Paragraph adminEmail = new Paragraph(admin.getEmail(), font12);

        Paragraph title = new Paragraph(reportName, boldFont);

        title.setAlignment(Element.ALIGN_CENTER);
        time.setAlignment(Element.ALIGN_RIGHT);
        adminFullName.setIndentationLeft(30f);
        adminEmail.setIndentationLeft(30f);

        document.add(time);
        document.add(adminText);
        document.add(adminFullName);
        document.add(adminEmail);
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

    private void fillDatabaseData(Document document, List<DatabaseHistoryDTO> history) throws DocumentException {
        PdfPTable table = new PdfPTable(4);

        Stream.of("Date", "Time", "Admin email", "Operation")
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

        for (DatabaseHistoryDTO historyRecord : history) {
            var dateCell = table.addCell(new PdfPCell(new Phrase(historyRecord.getTime()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yy")), font12)));

            var timeCell = table.addCell(new PdfPCell(new Phrase(historyRecord.getTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss")), font12)));

            var emailCell = table.addCell(new PdfPCell(new Phrase(historyRecord.getAdminEmail(), font12)));

            var operationCell = table.addCell(new PdfPCell(new Phrase(
                    historyRecord.getOperation().toString().toLowerCase(),
                    font12)));

            for (PdfPCell cell : List.of(dateCell, timeCell, emailCell, operationCell)) {
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
