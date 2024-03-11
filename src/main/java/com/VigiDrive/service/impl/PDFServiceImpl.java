package com.VigiDrive.service.impl;

import com.VigiDrive.service.PDFService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class PDFServiceImpl implements PDFService {

    @Override
    public ByteArrayOutputStream generateReport() throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        var todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.YYYY hh:mm"));
        Chunk chunk = new Chunk(todayDate, font);

        document.add(chunk);

        document.close();
        return outputStream;
    }
}
