package com.VigiDrive.exceptions.handlers;

import com.VigiDrive.exceptions.DriverLicenseException;
import com.VigiDrive.exceptions.dto.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DriverLicenseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DriverLicenseException.class)
    public ResponseEntity<Object> handleCustomerException(DriverLicenseException exception,
                                                          WebRequest webRequest) {
        var exceptionBody = new ExceptionResponse(exception.getName(), exception.getMessage());

        return handleExceptionInternal(exception, exceptionBody, new HttpHeaders(),
                exception.getResponseStatus(), webRequest);
    }
}