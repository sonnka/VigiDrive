package com.VigiDrive.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DurationValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Duration {
    String message() default "Invalid duration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
