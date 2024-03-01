package ru.yandex.practicum.filmorate.exception.storages.film;

import ru.yandex.practicum.filmorate.exception.storages.AlreadyExistException;

public class LikeFilmNotFoundException extends AlreadyExistException {

        public static final String LIKE_NOT_FOUND = "Лайк пользователя userID_%d для фильма filmID_%d не найден";

        public LikeFilmNotFoundException(String message) {
            super(message);

    }
}
