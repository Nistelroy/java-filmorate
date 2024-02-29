package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class StorageException extends RuntimeException{
    public StorageException(String message) {
        super(message);
    }
}
