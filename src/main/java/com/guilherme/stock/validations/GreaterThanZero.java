package com.guilherme.stock.validations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreaterThanZeroValidator.class)
public @interface GreaterThanZero {
    String message() default "O valor deve ser maior que zero";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class GreaterThanZeroValidator implements ConstraintValidator<GreaterThanZero, Double> {
    @Override
    public void initialize(GreaterThanZero constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value != null && value > 0;
    }
}