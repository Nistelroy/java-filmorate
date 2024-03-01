package ru.yandex.practicum.filmorate.exception.storages.user;

import ru.yandex.practicum.filmorate.exception.storages.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public static final String USER_NOT_FOUND = "Пользователь userID_%d не найден";

    public UserNotFoundException(String message) {
        super(message);
    }
}
