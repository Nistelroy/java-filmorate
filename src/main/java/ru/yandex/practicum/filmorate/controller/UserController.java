package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> userMap = new HashMap<>();
    private int id;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getId());
        user.setNameFromLogin();
        log.info("создание юзера: {}", user);
        userMap.put(user.getId(), user);

        return user;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User updatedUser) {
        if (userMap.get(updatedUser.getId()) != null) {
            log.info("замена юзера {} на обновлённого: {}", userMap.get(updatedUser.getId()), updatedUser);
            userMap.put(updatedUser.getId(), updatedUser);
            return updatedUser;
        } else {
            log.error("Ошибка обновления юзера {}", updatedUser);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>(userMap.values());
        log.info("запрос всех юзеров {}", userList.toString());
        return userList;
    }

    private int getId() {
        return ++id;
    }
}