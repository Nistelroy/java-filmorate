package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> createUser(User user);

    Optional<User> updateUser(User updatedUser);


    Collection<User> getAllUsers();

    Optional<User> getUser(int id);

    boolean userExist(int id);

    boolean userNotExist(int id);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    void confirmFriend(int id, int friendId);

    Collection<User> getFriends(int id);

    Collection<User> getCommonFriends(int id, int otherId);
}