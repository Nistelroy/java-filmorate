package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private List<User> users = new ArrayList<>();
    private static int id;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("; "));
            log.error("Ошибка валидации юзера: {}", errorMessage);

            return ResponseEntity.badRequest().body(user);
//            throw new ValidationException(errorMessage.toString());
        }
        user.setId(getId());
        log.info("создание юзера: {}", user);
        users.add(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {

        for (User user : users) {
            if (user.getId() == updatedUser.getId()) {
                log.info("замена юзера {} на обновлённого: {}", user, updatedUser);
                users.remove(user);
                users.add(updatedUser);
                return ResponseEntity.ok(updatedUser);
            }
        }
        log.error("Ошибка обновления юзера {}", updatedUser);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("запрос всех юзеров {}", users.toString());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private int getId() {
        id += 1;
        return id;
    }
}