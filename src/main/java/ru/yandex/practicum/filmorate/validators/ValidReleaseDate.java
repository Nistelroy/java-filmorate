package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReleaseDate {
    String message() default "дата релиза — не раньше 28 декабря 1895 года";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}