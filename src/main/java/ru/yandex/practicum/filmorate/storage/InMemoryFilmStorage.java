package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int id;

    @Override
    public Film addFilm(Film film) {
        film.setId(getId());
        if (filmMap.containsKey(film.getId())) {
            throw new ConflictException("Фильм уже существует");
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
            throw new ObjectNotFoundException("Фильм не существует");
        }
    }

    @Override
    public Film getFilmById(int filmId) {
        if (filmMap.get(filmId) == null) {
            throw new ObjectNotFoundException("Фильм не существует");
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