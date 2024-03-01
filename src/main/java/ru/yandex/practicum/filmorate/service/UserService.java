package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.service.user.UserException;
import ru.yandex.practicum.filmorate.exception.storages.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.service.user.UserException.UNABLE_FRIENDS_AMONG_THEMSELVES;
import static ru.yandex.practicum.filmorate.exception.service.user.UserException.UNABLE_TO_ADD_YOURSELF;
import static ru.yandex.practicum.filmorate.exception.service.user.UserException.UNABLE_TO_DELETE_YOURSELF;
import static ru.yandex.practicum.filmorate.exception.storages.user.UserNotFoundException.USER_NOT_FOUND;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new UserException(format(UNABLE_TO_ADD_YOURSELF, userId));
        }
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendId));
        }
        userStorage.getUserById(friendId).getFriends().add(userId);
        userStorage.getUserById(userId).getFriends().add(friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new UserException(format(UNABLE_TO_DELETE_YOURSELF, userId));
        }
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendId));
        }
        userStorage.getUserById(friendId).getFriends().remove(userId);
        userStorage.getUserById(userId).getFriends().remove(friendId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new UserException(format(UNABLE_FRIENDS_AMONG_THEMSELVES, userId));
        }
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendId));
        }
        User user1 = userStorage.getUserById(userId);
        User user2 = userStorage.getUserById(friendId);

        Set<Integer> user1Friends = user1.getFriends();
        Set<Integer> user2Friends = user2.getFriends();
        if (user1Friends.stream().anyMatch(user2Friends::contains)) {
            return user1Friends.stream()
                    .filter(user1Friends::contains)
                    .filter(user2Friends::contains)
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public Collection<User> getFriends(int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        if (userStorage.getUserById(userId).getFriends().isEmpty()) {
            return new ArrayList<>();
        }
        return
                userStorage.getUserById(userId)
                        .getFriends()
                        .stream()
                        .map(userStorage::getUserById)
                        .collect(Collectors.toList());

    }
}