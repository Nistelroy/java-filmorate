package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.storages.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.storages.film.LikeFilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storages.film.LikeFilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.storages.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storages.film.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storages.film.LikeFilmAlreadyExistsException.LIKE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storages.film.LikeFilmNotFoundException.LIKE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storages.user.UserNotFoundException.USER_NOT_FOUND;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmId));
        }
        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new LikeFilmAlreadyExistsException(format(LIKE_ALREADY_EXISTS, filmId, userId));
        }
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    public void unlikeFilm(int userId, int filmId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        if (filmStorage.getFilmById(filmId) == null) {
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmId));
        }
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            throw new LikeFilmNotFoundException(format(LIKE_NOT_FOUND, userId, filmId));
        }
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