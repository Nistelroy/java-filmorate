package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> userMap = new HashMap<>();
    private int id;

    @Override
    public User createUser(User user) {
        user.setId(getId());
        user.setNameFromLogin();
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int userId) {
        return userMap.get(userId);
    }

    @Override
    public User updateUser(User updatedUser) {
        if (userMap.containsKey(updatedUser.getId())) {
            userMap.put(updatedUser.getId(), updatedUser);
            return updatedUser;
        } else {
            throw new UserNotFoundException("Пользователь не найден", updatedUser);
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