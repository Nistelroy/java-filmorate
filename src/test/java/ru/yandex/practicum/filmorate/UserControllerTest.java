package ru.yandex.practicum.filmorate;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    protected void initializeUser() {
        user = User.builder()
                .email("mail@yandex.ru")
                .login("Boetticher")
                .name("Василий")
                .birthday(LocalDate.of(1987, 4, 14))
                .build();
    }

    @Test
    void testCreateNewUser_Success() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))

                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUserWithBlankName_NameIsLogin() throws Exception {
        user.setName("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))

                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value(""))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUserWithIncorrectEmail_BadRequest() throws Exception {
        user.setEmail("badEmail.ru");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserWithBlankLogin_BadRequest() throws Exception {
        user.setLogin("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUserWithFutureBirthday_BadRequest() throws Exception {
        user.setBirthday(LocalDate.parse("2024-10-12"));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}