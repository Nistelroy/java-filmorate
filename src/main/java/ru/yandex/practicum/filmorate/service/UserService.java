package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> createUser(User user) {
        return userStorage.createUser(user);
    }


    public Optional<User> updateUser(User user) {
        if (userStorage.userNotExist(user.getId())) {
            throw new ObjectNotFoundException("Пользователь с id " + user.getId() + " не найден.");
        }
        return userStorage.updateUser(user);
    }

    public Optional<User> getUser(int userId) {
        Optional<User> foundUser = userStorage.getUser(userId);
        if (userStorage.userNotExist(userId)) {
            throw new ObjectNotFoundException("Пользователь с id " + userId + " не найден.");
        }
        return foundUser;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ConflictException("Нельзя добавить в друзья себя");
        }
        if (userStorage.userNotExist(userId)) {
            throw new ObjectNotFoundException("Пользователь с id " + userId + " не найден.");
        }
        if (userStorage.userNotExist(friendId)) {
            throw new ObjectNotFoundException("Пользователь с id " + friendId + " не найден.");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ConflictException("Нельзя удалить из друзей себя");
        }
        if (userStorage.userNotExist(userId)) {
            throw new ObjectNotFoundException("Пользователь с id " + userId + " не найден.");
        }
        if (userStorage.userNotExist(friendId)) {
            throw new ObjectNotFoundException("Пользователь с id " + friendId + " не найден.");
        }
        userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public void confirmFriend(int userId, int friendId) {
        if (userStorage.userNotExist(userId)) {
            throw new ObjectNotFoundException("Пользователь с id " + userId + " не найден.");
        }
        if (userStorage.userNotExist(friendId)) {
            throw new ObjectNotFoundException("Пользователь с id " + friendId + " не найден.");
        }
        userStorage.confirmFriend(userId, friendId);
    }

    public Collection<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }
}
