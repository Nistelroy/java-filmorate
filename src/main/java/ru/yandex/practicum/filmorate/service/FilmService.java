package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Collection<Film> getPopularFilm(int count) {
        return filmStorage.getAllFilms()
                        .stream()
                        .sorted(Comparator.comparingInt(this::getLikeCount).reversed())
                        .limit(count)
                        .collect(Collectors.toList());
    }

    public void likeFilm(int userId, int filmId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        filmStorage.getFilmById(filmId).getLikes().remove(userId);
    }

    public Collection<Film> getTopLikedFilms() {
        List<Film> allFilms = filmStorage.getAllFilms();

        allFilms.sort((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()));

        return allFilms.subList(0, Math.min(10, allFilms.size()));
    }

    private int getLikeCount(Film film) {
        return film.getLikes().size();
    }

}