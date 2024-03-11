package com.VigiDrive.service;

import com.itextpdf.text.DocumentException;

import java.io.ByteArrayOutputStream;

public interface PDFService {

    ByteArrayOutputStream generateReport() throws DocumentException;
}
