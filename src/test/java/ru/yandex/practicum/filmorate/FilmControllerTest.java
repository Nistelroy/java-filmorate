package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Film film;

    @BeforeEach
    protected void initTests() {
        film = Film.builder()
                .name("случайный фильм")
                .description("это очень старый фильм")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(90)
                .build();
    }

    @Test
    void testCreateNewFilm_Success() throws Exception {
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateFilmWithFutureReleaseDate_Success() throws Exception {
        film.setReleaseDate(LocalDate.of(2026, 12, 9));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateFilmWithBlankName_BadRequest() throws Exception {
        film.setName("");
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFilmWithReleaseDateBeforeFirstFilmDate_BadRequest() throws Exception {
        film.setReleaseDate(LocalDate.of(1685, 12, 12));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateFilmWithLongDescription_BadRequest() throws Exception {
        film.setDescription("------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------" +
                "----------------------------------------------------------------------------------------------");

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }


}