package com.VigiDrive.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SituationTypeValidator implements ConstraintValidator<SituationType, String> {
    @Override
    public boolean isValid(String situationType, ConstraintValidatorContext cxt) {
        boolean valid = situationType.equals(com.VigiDrive.model.enums.SituationType.LOW_SPEED.name())
                || situationType.equals(com.VigiDrive.model.enums.SituationType.HIGH_SPEED.name())
                || situationType.equals(com.VigiDrive.model.enums.SituationType.FALLING_ASLEEP.name())
                || situationType.equals(com.VigiDrive.model.enums.SituationType.HIGH_LEVEL_OF_FATIGUE.name())
                || situationType.equals(com.VigiDrive.model.enums.SituationType.DEVIATION_FROM_ROUT.name());
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "SituationType is invalid");
        }
        return true;
    }
}