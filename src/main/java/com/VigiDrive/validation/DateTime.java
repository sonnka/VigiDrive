package com.VigiDrive.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTime {
    String message() default "Invalid time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
