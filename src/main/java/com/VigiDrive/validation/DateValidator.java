package com.VigiDrive.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext cxt) {
        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Date is invalid!");
        }
        return true;
    }
}
