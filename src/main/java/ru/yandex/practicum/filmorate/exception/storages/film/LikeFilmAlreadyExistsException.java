package ru.yandex.practicum.filmorate.exception.storages.film;

import ru.yandex.practicum.filmorate.exception.storages.AlreadyExistException;

public class LikeFilmAlreadyExistsException extends AlreadyExistException {
    public static final String LIKE_ALREADY_EXISTS = "Пользователь userID_%d уже ставил лайк фильму filmID_%d";

    public LikeFilmAlreadyExistsException(String message) {
        super(message);
    }
}
