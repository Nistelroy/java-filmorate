package ru.yandex.practicum.filmorate.exceptions;

public class InternalServiceException extends Exception {
    public InternalServiceException(final String text) {
        super(text);
    }
}