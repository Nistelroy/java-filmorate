package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;

    @NotBlank(message = "название не может быть пустым")
    private String name;

    @NotBlank(message = "описание не может быть пустым")
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    @ValidReleaseDate
    @NotNull
    private LocalDate releaseDate;

    @Positive(message = "продолжительность фильма должна быть положительной.")
    private int duration;

    private Set<Integer> likes;

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }
}