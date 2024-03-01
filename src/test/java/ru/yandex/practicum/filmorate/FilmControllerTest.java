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
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @Test
    void testCreateFilmWithBlankName_BadRequest() {
        film.setName("");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректно указано название фильма.", e.getMessage());
        }
    }

    @Test
    void testCreateFilmWithReleaseDateBeforeFirstFilmDate_BadRequest() {
        film.setReleaseDate(LocalDate.of(1685, 12, 12));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Некорректно указана дата релиза.", e.getMessage());
        }
    }

    @Test
    void testCreateFilmWithLongDescription_BadRequest() {
        film.setDescription("------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------");
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Превышено количество символов в описании фильма.", e.getMessage());
        }
    }
}