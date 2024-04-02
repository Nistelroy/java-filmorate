package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user).get();
    }


    public User updateUser(User user) {
        if (!userStorage.userExist(user.getId())) {
            throw new ObjectNotFoundException("Пользователь с id " + user.getId() + " не найден.");
        }
        return userStorage.updateUser(user).get();
    }

    public User getUser(int userId) {
        return userStorage.getUser(userId).get();
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriends(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(int userId, int friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    public void confirmFriend(int userId, int friendId) {
        userStorage.confirmFriend(userId, friendId);
    }

    public Collection<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }
}
