package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.User;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final User user;

    public UserNotFoundException(String message, User user) {
        super(message);
        this.user = user;
    }
}
