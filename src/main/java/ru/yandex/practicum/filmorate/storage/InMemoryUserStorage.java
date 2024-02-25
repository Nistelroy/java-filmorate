package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void deleteUser(int id) {
        userMap.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        if (user != null && friend != null) {
            user.addFriend(friend.getId());
            friend.addFriend(user.getId());
        } else {
            throw new UserNotFoundException("Пользователь или друг не найден", null);
        }
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        if (user != null && friend != null) {
            user.removeFriend(friend.getId());
            friend.removeFriend(user.getId());
        } else {
            throw new UserNotFoundException("Пользователь или друг не найден", null);
        }
    }

    @Override
    public Set<Integer> getFriends(int userId) {
        User user = userMap.get(userId);
        if (user != null) {
            return user.getFriends();
        } else {
            throw new UserNotFoundException("Пользователь не найден", null);
        }
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userMap.get(userId);
        User otherUser = userMap.get(otherUserId);
        if (user != null && otherUser != null) {
            Set<Integer> userFriendsIds = user.getFriends();
            Set<Integer> otherUserFriendsIds = otherUser.getFriends();

            Set<Integer> commonFriendIds = new HashSet<>(userFriendsIds);
            commonFriendIds.retainAll(otherUserFriendsIds);

            List<User> commonFriends = new ArrayList<>();
            for (Integer friendId : commonFriendIds) {
                User commonFriend = userMap.get(friendId);
                if (commonFriend != null) {
                    commonFriends.add(commonFriend);
                }
            }

            return commonFriends;
        } else {
            throw new UserNotFoundException("Пользователь или друг не найден", null);
        }
    }

    private int getId() {
        return ++id;
    }
}