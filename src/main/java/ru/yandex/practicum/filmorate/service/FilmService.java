package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.model.Genre;


import java.util.Collection;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Optional<Film> addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        if (filmStorage.filmNotExist(film.getId())) {
            throw new ObjectNotFoundException("Фильм с id " + film.getId() + " не найден.");
        }
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> getFilm(int filmId) {
        Optional<Film> foundFilm = filmStorage.getFilm(filmId);
        if (foundFilm.isEmpty()) {
            throw new ObjectNotFoundException("Фильм с id " + filmId + " не найден.");
        }
        return foundFilm;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Collection<Film> getPopularFilm(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void likeFilm(int filmId, int userId) {
        if (filmStorage.getFilm(filmId).isEmpty()) {
            throw new ObjectNotFoundException("Фильм с айди'" + filmId + "' не найден");
        }
        filmStorage.getFilm(filmId).get().addLike(userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        if (filmStorage.getFilm(filmId).isEmpty()) {
            throw new ObjectNotFoundException("Фильм с айди'" + filmId + "' не найден");
        }
        filmStorage.getFilm(filmId).get().removeLike(userId);
    }

    public Collection<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Optional<Mpa> getMpaById(int id) {
        Optional<Mpa> mpa = filmStorage.getMpaById(id);
        if (mpa.isEmpty()) {
            throw new ObjectNotFoundException("Рейтинга с id " + id + " не существует.");
        }
        return mpa;
    }

    public Collection<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Optional<Genre> getGenreById(int id) {
        Optional<Genre> genre = filmStorage.getGenreById(id);
        if (genre.isEmpty()) {
            throw new ObjectNotFoundException("Жанра с id " + id + " не существует.");
        }
        return genre;
    }
}