package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film updatedFilm);

    Collection<Film> getAllFilms();

    Optional<Film> getFilm(int id);
    boolean filmExist(int id);

    boolean filmNotExist(int id);

    void addLike(int id, int userId);

    void removeLike(int id, int userId);

    Collection<Film> getPopularFilms(int count);

    Collection<Mpa> getAllMpa();

    Optional<Mpa> getMpaById(int id);

    Collection<Genre> getAllGenres();

    Optional<Genre> getGenreById(int id);
}