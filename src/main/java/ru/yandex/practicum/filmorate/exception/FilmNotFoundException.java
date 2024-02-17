package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FilmNotFoundException extends ResponseStatusException {

    private final String message;

    public FilmNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.message = message;
    }
}