package ru.yandex.practicum.filmorate.exception.storages.user;

import ru.yandex.practicum.filmorate.exception.storages.AlreadyExistException;

public class UserAlreadyExistsException extends AlreadyExistException {
    public static final String USER_ALREADY_EXISTS = "Пользователь userID_%d уже был добавлен ранее";

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}