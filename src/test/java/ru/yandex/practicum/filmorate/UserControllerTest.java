package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    protected void initializeUser() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        user = User.builder()
                .email("mail@yandex.ru")
                .login("Boetticher")
                .name("Василий")
                .birthday(LocalDate.of(1987, 4, 14))
                .build();
    }

    @Test
    void testCreateUserWithBlankName_NameIsLogin() {
        user.setName("");
        userController.createUser(user);
        assertEquals("Boetticher", userController.getAllUsers().get(0).getName());
    }

    @Test
    void testCreateUserWithIncorrectEmail_BadRequest() {
        user.setEmail("badEmail.ru");
        try {
            userController.createUser(user);
        } catch (ValidationException e) {
            assertEquals("Неверно указана эмейл", e.getMessage());
        }
    }

    @Test
    void testCreateUserWithFutureBirthday_BadRequest() {
        user.setBirthday(LocalDate.parse("2024-10-12"));
        try {
            userController.createUser(user);
        } catch (ValidationException e) {
            assertEquals("Неверно указана дата рождения", e.getMessage());
        }
    }
}