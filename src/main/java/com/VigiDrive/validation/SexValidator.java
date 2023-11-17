package com.VigiDrive.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SexValidator implements ConstraintValidator<Sex, String> {
    @Override
    public boolean isValid(String sex, ConstraintValidatorContext cxt) {
        boolean valid = sex.equals(com.VigiDrive.model.enums.Sex.MAN.name())
                || sex.equals(com.VigiDrive.model.enums.Sex.WOMAN.name());
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Sex must be MAN or WOMAN!");
        }
        return true;
    }
}
