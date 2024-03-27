package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> createUser(User user) {
        Map<String, Object> userMap = user.toMap();

        String userName = user.getName();
        if (!StringUtils.hasText(userName)) {
            userName = user.getLogin();
            userMap.put("name", userName);
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        int userId = simpleJdbcInsert.executeAndReturnKey(userMap).intValue();

        User newUser = User.builder()
                .id(userId)
                .login(user.getLogin())
                .email(user.getEmail())
                .name(userName)
                .birthday(user.getBirthday())
                .friends(new HashSet<>())
                .build();

        return Optional.of(newUser);
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery =
                "update users set " +
                        "login = ?, email = ?, name = ?, birthday = ? " +
                        "where user_id = ?";

        int rowCount = jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (rowCount == 0) {
            return Optional.empty();
        }

        User newUser = User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .birthday(user.getBirthday())
                .friends(user.getFriends())
                .build();

        return Optional.of(newUser);
    }

    @Override
    public Optional<User> getUser(int id) {
        return getUserById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public boolean userExist(int id) {
        String sqlQuery = "select 1 from users where user_id = ? limit 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlQuery, id);
        return result.next();
    }

    @Override
    public boolean userNotExist(int id) {
        return !userExist(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sqlQuery = "merge into friends(user_id, friend_id, status) key(user_id, friend_id) values(?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, 0);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void confirmFriend(int userId, int friendId) {
        String sqlQuery = "update friends set " +
                "status = ? " +
                "where user_id = ? and friend_id = ?; " +
                "merge into friends(user_id, friend_id, status) key(user_id, friend_id) values(?, ?, ?)";

        jdbcTemplate.update(sqlQuery, 1, userId, friendId, friendId, userId, 1);
    }

    @Override
    public Collection<User> getFriends(int id) {
        String sqlQuery = "select * from users where user_id in (select friend_id from friends where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        String sqlQuery = "select * from users where user_id in (" +
                "select friends.friend_id from friends as friends " +
                "inner join friends as other_friends " +
                "on friends.friend_id = other_friends.friend_id "  +
                "where  friends.user_id = ? and other_friends.user_id = ?" +
                ")";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
    }

    private Optional<User> getUserById(int id) {
        String sqlQuery = "select * from users where user_id = ?";
        Collection<User> users =  jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        return users.stream().findFirst();
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(new HashSet<>())
                .build();
    }

}