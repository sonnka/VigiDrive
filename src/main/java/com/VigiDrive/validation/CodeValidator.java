package com.VigiDrive.validation;

import com.VigiDrive.model.enums.CountryCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CodeValidator implements ConstraintValidator<Code, String> {
    @Override
    public boolean isValid(String countryCode, ConstraintValidatorContext cxt) {
        boolean valid = countryCode.equals(CountryCode.UK.name())
                || countryCode.equals(CountryCode.US.name())
                || countryCode.equals(CountryCode.UA.name())
                || countryCode.equals(CountryCode.AU.name())
                || countryCode.equals(CountryCode.CA.name())
                || countryCode.equals(CountryCode.GE.name());
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Country code must be UK, US, UA, AU, CA or GE");
        }
        return true;
    }
}
