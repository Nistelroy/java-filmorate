package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriends(int userId, int friendId) {
        if (userId == friendId) {
            throw new ConflictException("Нельзя добавить в друзья себя");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + userId + " не существует");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + friendId + " не существует");
        }
        userStorage.getUserById(friendId).addFriend(userId);
        userStorage.getUserById(userId).addFriend(friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ConflictException("Нельзя удалить из друзей себя");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + userId + " не существует");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + friendId + " не существует");
        }
        userStorage.getUserById(friendId).removeFriend(userId);
        userStorage.getUserById(userId).removeFriend(friendId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + userId + " не существует");
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + friendId + " не существует");
        }

        User user1 = userStorage.getUserById(userId);
        User user2 = userStorage.getUserById(friendId);

        Set<Integer> user1Friends = new HashSet<>(user1.getFriends());
        Set<Integer> user2Friends = user2.getFriends();

        user1Friends.retainAll(user2Friends);

        if (user1Friends.isEmpty()) {
            return new ArrayList<>();
        } else {
            return user1Friends.stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());
        }
    }

    public List<User> getFriends(int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ObjectNotFoundException("Пользователь с айди : " + userId + " не существует");
        }
        if (userStorage.getUserById(userId).getFriends().isEmpty()) {
            throw new ObjectNotFoundException("У пользователя нет друзей");
        }
        return userStorage.getUserById(userId)
                .getFriends()
                .stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());

    }
}