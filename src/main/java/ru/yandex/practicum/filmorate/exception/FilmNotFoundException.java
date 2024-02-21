package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.Film;

@Getter
public class FilmNotFoundException extends RuntimeException {

    private final Film film;

    public FilmNotFoundException(String message, Film film) {
        super(message);
        this.film = film;
    }
}