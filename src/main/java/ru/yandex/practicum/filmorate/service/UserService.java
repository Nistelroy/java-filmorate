package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
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

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        userStorage.getUserById(friendId).getFriends().add(userId);
        userStorage.getUserById(userId).getFriends().add(friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.getUserById(friendId).getFriends().remove(userId);
        userStorage.getUserById(userId).getFriends().remove(friendId);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);

        Set<Integer> user1Friends = user1.getFriends();
        Set<Integer> user2Friends = user2.getFriends();

        return user1Friends.stream()
                .filter(user2Friends::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getFriends(int userID) {
        return
                userStorage.getAllUsers()
                        .get(userID)
                        .getFriends()
                        .stream()
                        .map(userStorage::getUserById)
                        .collect(Collectors.toList());

    }
}