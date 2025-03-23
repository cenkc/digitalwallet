package com.cenkc.digitalwallet.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TCKNValidatorImpl.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TCKN {
    String message() default "Invalid TCKN. It must be exactly 11 digits.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}