package ru.yandex.practicum.filmorate.exception.storages.film;

import ru.yandex.practicum.filmorate.exception.storages.NotFoundException;

public class FilmNotFoundException extends NotFoundException {
    public static final String FILM_NOT_FOUND = "Фильм filmID_%d не найден";

    public FilmNotFoundException(String message) {
        super(message);
    }
}