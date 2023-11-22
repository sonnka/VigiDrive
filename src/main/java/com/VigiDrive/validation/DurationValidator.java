package com.VigiDrive.validation;

import com.VigiDrive.model.enums.TimeDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DurationValidator implements ConstraintValidator<Duration, String> {
    @Override
    public boolean isValid(String duration, ConstraintValidatorContext cxt) {
        boolean valid = duration.equals(TimeDuration.DAY.name())
                || duration.equals(TimeDuration.WEEK.name())
                || duration.equals(TimeDuration.TWO_WEEKS.name())
                || duration.equals(TimeDuration.MONTH.name())
                || duration.equals(TimeDuration.SIX_MONTH.name())
                || duration.equals(TimeDuration.YEAR.name());
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "SituationType is invalid");
        }
        return true;
    }
}