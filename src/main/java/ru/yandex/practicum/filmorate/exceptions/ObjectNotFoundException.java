package ru.yandex.practicum.filmorate.exceptions;

public class ObjectNotFoundException extends IllegalArgumentException {
    public ObjectNotFoundException(final String text) {
        super(text);
    }
}
