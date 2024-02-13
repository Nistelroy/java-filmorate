package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ValidReleaseDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        if (releaseDate == null) {
            return true;
        }
        return releaseDate.isEqual(EARLIEST_RELEASE_DATE) || releaseDate.isAfter(EARLIEST_RELEASE_DATE);
    }
}