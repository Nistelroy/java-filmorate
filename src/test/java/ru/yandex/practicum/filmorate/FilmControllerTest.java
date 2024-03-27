package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private FilmStorage filmStorage;
    private FilmController filmController;
    private Film film;

    @BeforeEach
    protected void initTests() {
        filmStorage = new InMemoryFilmStorage();
        filmController = new FilmController(new FilmService(filmStorage));
        film = Film.builder()
                .name("случайный фильм")
                .description("это очень старый фильм")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(90)
                .build();
    }

    @Test
    void testCreateNewFilm_Success() {
        filmController.addFilm(film);
        assertEquals(film, filmStorage.getFilmById(1));
    }
}