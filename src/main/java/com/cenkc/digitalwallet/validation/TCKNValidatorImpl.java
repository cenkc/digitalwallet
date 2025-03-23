package com.cenkc.digitalwallet.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class TCKNValidatorImpl implements ConstraintValidator<TCKN, String> {

    // TCKN can NOT start with zero (0) and length must be 11 digits
    public static final String TCKN_SIMPLE_REGEX = "^[1-9][0-9]{10}$";

    @Override
    public void initialize(TCKN constraintAnnotation) {
        // TODO check if its necessary
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String tckn, ConstraintValidatorContext ctx) {
        // for simplicity, using only length and first digit checks through regex 'TCKN_SIMPLE_REGEX'
        // if needed, one can provide/implement a proper algorithm
        Pattern pattern = Pattern.compile(TCKN_SIMPLE_REGEX);
        return pattern.matcher(tckn).matches();
    }
}
