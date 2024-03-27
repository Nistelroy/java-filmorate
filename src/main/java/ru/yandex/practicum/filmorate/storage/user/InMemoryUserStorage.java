package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();
    private int id = 0;

    @Override
    public Optional<User> createUser(User user) {
        user.setId(getId());
        user.setNameFromLogin();
        userMap.put(user.getId(), user);

        return Optional.of(user);
    }

    @Override
    public Optional<User> getUser(int userId) {
        if (userMap.get(userId) == null) {
            throw new ObjectNotFoundException("Юзер не существует");
        }

        return Optional.of(userMap.get(userId));
    }

    @Override
    public Optional<User> updateUser(User updatedUser) {
        if (userMap.get(updatedUser.getId()) != null) {
            userMap.put(updatedUser.getId(), updatedUser);

            return Optional.of(updatedUser);
        } else {
            throw new ObjectNotFoundException("Юзер не существует");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    @Override
    public boolean userExist(int id) {
        return userMap.containsKey(id);
    }

    @Override
    public boolean userNotExist(int id) {
        return !userMap.containsKey(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        Optional<User> userOptional = getUserById(userId);
        Optional<User> friendOptional = getUserById(friendId);
        userOptional.ifPresent(user -> user.addFriend(friendId));
        friendOptional.ifPresent(user -> user.addFriend(userId));
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        Optional<User> userOptional = getUserById(userId);
        Optional<User> friendOptional = getUserById(friendId);
        userOptional.ifPresent(user -> user.removeFriend(friendId));
        friendOptional.ifPresent(user -> user.removeFriend(userId));
    }

    @Override
    public Collection<User> getFriends(int id) {
        Set<Integer> friendsId = getUserById(id).get().getFriends();
        return getUsersByIds(friendsId);
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        Set<Integer> friendsId = new HashSet<>(getUserById(id).get().getFriends());
        Set<Integer> otherFriendsId = getUserById(otherId).get().getFriends();

        friendsId.retainAll(otherFriendsId);
        return getUsersByIds(friendsId);
    }

    @Override
    public void confirmFriend(int id, int friendId) {

    }

    private Optional<User> getUserById(int id) {
        if (!userMap.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(userMap.get(id));
    }

    private Collection<User> getUsersByIds(Set<Integer> usersId) {
        Collection<User> userList = new ArrayList<>();
        usersId.forEach(id -> userList.add(userMap.get(id)));
        return userList;
    }

    private int getId() {
        return ++id;
    }
}