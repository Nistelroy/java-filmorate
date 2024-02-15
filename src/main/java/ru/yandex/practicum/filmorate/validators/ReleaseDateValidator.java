package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {
    private LocalDate firstReleaseDate;

    @Override
    public void initialize(ValidReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        firstReleaseDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        return releaseDate == null || !releaseDate.isBefore(firstReleaseDate);
    }
}

