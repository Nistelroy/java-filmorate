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
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Integer, Film> filmMap = new HashMap<>();
    private int id;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(getId());
        log.info("создание фильма: {}", film);
        filmMap.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        if (filmMap.get(updatedFilm.getId()) == null) {
            log.error("Ошибка обновления фильма {}", updatedFilm);
            throw new FilmNotFoundException("Фильм не найден");
        }
        log.info("замена фильма {} на обновлённый: {}", filmMap.get(updatedFilm.getId()), updatedFilm);
        filmMap.put(updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>(filmMap.values());
        log.info("запрос всех фильмов {}", films.toString());
        return films;
    }

    private int getId() {
        return ++id;
    }
}