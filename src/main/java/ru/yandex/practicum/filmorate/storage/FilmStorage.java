package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film updatedFilm);
    void deleteFilm(int id);
    List<Film> getAllFilms();
    Film getFilmById(int id);
    void likeFilm(int filmId, int userId);
    void unlikeFilm(int filmId, int userId);
}