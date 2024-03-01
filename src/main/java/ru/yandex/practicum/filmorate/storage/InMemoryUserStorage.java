package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @Override
    public User createUser(User user) {
        user.setId(getId());
        user.setNameFromLogin();
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public User getUserById(int userId) {
        if (userMap.get(userId) == null) {
            throw new ObjectNotFoundException("Юзер не существует");
        }
        return userMap.get(userId);
    }

    @Override
    public User updateUser(User updatedUser) {
        if (userMap.get(updatedUser.getId()) != null) {
            userMap.put(updatedUser.getId(), updatedUser);
            return updatedUser;
        } else {
            throw new ObjectNotFoundException("Юзер не существует");
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