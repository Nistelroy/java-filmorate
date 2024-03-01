package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(final String text) {
        super(text);
    }

}
