package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
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

    private Set<Integer> friends;

    private Set<Integer> likedFilms;

    public void setNameFromLogin() {
        if (name == null) {
            name = login;
        }
    }

    public void likeFilm(Film film) {
        likedFilms.add(film.getId());
        film.addLike(getId());
    }

    public void unlikeFilm(Film film) {
        likedFilms.remove(film.getId());
        film.removeLike(getId());
    }

    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }
}