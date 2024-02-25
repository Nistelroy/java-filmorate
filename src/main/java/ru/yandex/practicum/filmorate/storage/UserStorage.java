package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User updatedUser);
    void deleteUser(int id);
    List<User> getAllUsers();
    User getUserById(int id);
    void addFriend(int userId, int friendId);
    void removeFriend(int userId, int friendId);
    Set<Integer> getFriends(int userId);
    List<User> getCommonFriends(int userId, int otherUserId);
}