package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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

    private Mpa mpa;

    @ValidReleaseDate
    @NotNull
    private LocalDate releaseDate;

    @Positive(message = "продолжительность фильма должна быть положительной.")
    private int duration;

    private Set<Integer> likes;

    private Set<Genre> genres;

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }

    public Integer getMpaId() {
        if (mpa == null) {
            throw new IllegalStateException("Мпа не может быть пустым для фильма.");
        }
        return mpa.getId();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa_id", getMpaId());
        return values;
    }
}
