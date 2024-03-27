package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmMap = new ConcurrentHashMap<>();
    private int id;

    @Override
    public Optional<Film> addFilm(Film film) {
        film.setId(getId());
        if (filmMap.containsKey(film.getId())) {
            throw new ConflictException("Фильм уже существует");
        }

        filmMap.put(film.getId(), film);

        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film updatedFilm) {
        if (filmMap.get(updatedFilm.getId()) != null) {
            filmMap.put(updatedFilm.getId(), updatedFilm);

            return Optional.of(updatedFilm);
        } else {
            throw new ObjectNotFoundException("Фильм не существует");
        }
    }

    @Override
    public Optional<Film> getFilm(int filmId) {
        if (filmMap.get(filmId) == null) {
            throw new ObjectNotFoundException("Фильм не существует");
        }

        return Optional.of(filmMap.get(filmId));
    }

    @Override
    public Collection<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public boolean filmExist(int id) {
        return filmMap.containsKey(id);
    }

    @Override
    public boolean filmNotExist(int id) {
        return !filmMap.containsKey(id);
    }

    @Override
    public void addLike(int id, int userId){
        Optional<Film> film = getFilmById(id);
        film.get().addLike(userId);
    }

    @Override
    public void removeLike(int id, int userId){
        Optional<Film> film = getFilmById(id);
        film.get().removeLike(userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return filmMap.values().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return null;
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return Optional.empty();
    }

    private Optional<Film> getFilmById(int id) {
        if (!filmMap.containsKey(id)) {
            throw new ObjectNotFoundException("Фильма с id " + id + " не существует.");
        }
        return Optional.of(filmMap.get(id));
    }
    private int getId() {
        return ++id;
    }
}