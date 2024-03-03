package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Assertions.assertEquals(film, filmStorage.getFilmById(1));
    }

    @Test
    void testCreateFilmWithFutureReleaseDate_Success() {
        film.setReleaseDate(LocalDate.of(2026, 12, 9));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testCreateFilmWithBlankName_BadRequest() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testCreateFilmWithReleaseDateBeforeFirstFilmDate_BadRequest() {
        film.setReleaseDate(LocalDate.of(1685, 12, 12));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testCreateFilmWithLongDescription_BadRequest() {
        film.setDescription("------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }
}