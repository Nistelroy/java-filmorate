package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private List<Film> films = new ArrayList<>();
    private int id;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("; "));
            log.error("Ошибка валидации фильма : {}", errorMessage);
            return ResponseEntity.badRequest().body(film);
//            throw new ValidationException(errorMessage.toString());
        }
        film.setId(getId());
        log.info("создание фильма: {}", film);
        films.add(film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film updatedFilm) {

        for (Film film : films) {
            if (film.getId() == updatedFilm.getId()) {
                log.info("замена фильма {} на обновлённый: {}",film, updatedFilm);
                updatedFilm.setId(film.getId());
                films.remove(film);
                films.add(updatedFilm);
                return ResponseEntity.ok(updatedFilm);
            }
        }
        log.error("Ошибка обновления фильма {}", updatedFilm);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.info("запрос всех юзеров {}", films.toString());
        return new ResponseEntity<>(films, HttpStatus.OK);
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
