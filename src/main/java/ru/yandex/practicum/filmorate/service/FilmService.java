package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public void deleteFilmById(int id) {
        filmStorage.deleteFilm(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getAllFilms()
                .stream()
                .sorted((film1, film2) ->
                        film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void likeFilm(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new ObjectNotFoundException("Фильм с айди'" + filmId + "' не найден");
        }
        filmStorage.getFilmById(filmId).addLike(userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new ObjectNotFoundException("Фильм с айди'" + filmId + "' не найден");
        }
        filmStorage.getFilmById(filmId).removeLike(userId);
    }
}