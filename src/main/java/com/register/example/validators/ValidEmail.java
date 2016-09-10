package com.register.example.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = com.register.example.validators.EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "{ValidEmail}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}