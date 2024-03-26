package ru.yandex.practicum.filmorate.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(final String text) {
        super(text);
    }
}