package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.storages.user.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storages.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storages.user.UserAlreadyExistsException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storages.user.UserNotFoundException.USER_NOT_FOUND;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @Override
    public User createUser(User user) {
        user.setId(getId());
        user.setNameFromLogin();
        if (userMap.get(user.getId()) != null) {
            throw new UserAlreadyExistsException(format(USER_ALREADY_EXISTS, user.getId()));
        }
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public User getUserById(int userId) {
        if (userMap.get(userId) == null) {
            throw new UserNotFoundException(format(USER_NOT_FOUND, userId));
        }
        return userMap.get(userId);
    }

    @Override
    public User updateUser(User updatedUser) {
        if (userMap.get(updatedUser.getId()) != null) {
            userMap.put(updatedUser.getId(), updatedUser);
            return updatedUser;
        } else {
            throw new UserNotFoundException(format(USER_NOT_FOUND, updatedUser.getId()));
        }
    }

    @Override
    public User deleteUser(int id) {
        User user = userMap.get(id);
        userMap.remove(id);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    private int getId() {
        return ++id;
    }
}