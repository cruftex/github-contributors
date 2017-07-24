package com.newrelic.contributors.domain;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.IntStream;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = Top.Validator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
public @interface Top {

    String message() default "{com.newrelic.contributors.domain.Top.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<Top, Integer> {

        @Override
        public void initialize(final Top constraintAnnotation) {
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
            return IntStream.of(50, 100, 150).boxed().anyMatch(i -> i.equals(value));
        }

    }

}
