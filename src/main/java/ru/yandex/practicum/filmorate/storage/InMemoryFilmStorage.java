package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> filmMap = new HashMap<>();
    private int id;

    @Override
    public Film addFilm(Film film) {
        film.setId(getId());
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        if (filmMap.containsKey(updatedFilm.getId())) {
            filmMap.put(updatedFilm.getId(), updatedFilm);
            return updatedFilm;
        } else {
            throw new FilmNotFoundException("Фильм не найден", updatedFilm);
        }
    }

    @Override
    public Film getFilmById(int filmId) {
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