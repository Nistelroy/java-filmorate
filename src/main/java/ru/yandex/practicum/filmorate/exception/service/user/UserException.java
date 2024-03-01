package ru.yandex.practicum.filmorate.exception.service.user;


import ru.yandex.practicum.filmorate.exception.service.ServiceException;

public class UserException extends ServiceException {
    public static final String UNABLE_TO_ADD_YOURSELF =
            "Пользователь userID_%d не может добавить сам себя в друзья";
    public static final String UNABLE_TO_DELETE_YOURSELF =
            "Пользователь userID_%d не может удалить себя из друзей";
    public static final String UNABLE_FRIENDS_AMONG_THEMSELVES =
            "Пользователь userID_%d не может запросить общих друзей между собой";

    public UserException(String message) {
        super(message);
    }
}

