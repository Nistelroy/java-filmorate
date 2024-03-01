package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.storages.film.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.storages.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storages.film.FilmAlreadyExistException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storages.film.FilmNotFoundException.FILM_NOT_FOUND;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> filmMap = new HashMap<>();
    private int id;

    @Override
    public Film addFilm(Film film) {
        film.setId(getId());
        if (filmMap.containsKey(film.getId())) {
            throw new FilmAlreadyExistException(format(FILM_ALREADY_EXISTS, film.getId()));
        }
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        if (filmMap.get(updatedFilm.getId()) != null) {
            filmMap.put(updatedFilm.getId(), updatedFilm);
            return updatedFilm;
        } else {
             throw new FilmNotFoundException(format(FILM_NOT_FOUND, updatedFilm.getId()));
        }
    }

    @Override
    public Film getFilmById(int filmId) {
        if (filmMap.get(filmId) == null) {
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmId));
        }
        return filmMap.get(filmId);
    }

    @Override
    public Film deleteFilm(int id) {
        Film film = filmMap.get(id);
        filmMap.remove(id);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    private int getId() {
        return ++id;
    }
}