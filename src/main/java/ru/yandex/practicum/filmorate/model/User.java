package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {


    @NotBlank(message = "логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "логин не может содержать пробелы")
    private String login;

    private String name;
    private int id;
    @NotBlank(message = "электронная почта не может быть пустой")
    @Email(message = "электронная почта должна содержать символ @ и соответствовать формату d@d.d")
    private String email;

    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;

    public String getName() {
        if (name == null) {
            return login;
        } else return name;
    }
}
