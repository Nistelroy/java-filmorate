package ru.yandex.practicum.filmorate.exception.storages.film;

import ru.yandex.practicum.filmorate.exception.storages.AlreadyExistException;

public class FilmAlreadyExistException extends AlreadyExistException {
    public static final String FILM_ALREADY_EXISTS = "Фильм filmID_%d уже был добавлен ранее";

    public FilmAlreadyExistException(String message) {
        super(message);
    }
}
