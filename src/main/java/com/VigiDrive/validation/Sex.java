package com.VigiDrive.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SexValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sex {
    String message() default "Invalid sex";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
