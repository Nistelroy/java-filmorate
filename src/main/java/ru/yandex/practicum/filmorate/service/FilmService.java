package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(int userId, int filmId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);

        if (!user.getLikedFilms().contains(film.getId())) {
            user.likeFilm(film);
            film.addLike(userId);

            userStorage.updateUser(user);
            filmStorage.updateFilm(film);
        }
    }

    public void unlikeFilm(int userId, int filmId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);

        if (user.getLikedFilms().contains(film.getId())) {
            user.unlikeFilm(film);
            film.removeLike(userId);

            userStorage.updateUser(user);
            filmStorage.updateFilm(film);
        }
    }

    public List<Film> getTopLikedFilms() {
        List<Film> allFilms = filmStorage.getAllFilms();

        allFilms.sort((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()));

        return allFilms.subList(0, Math.min(10, allFilms.size()));
    }


}