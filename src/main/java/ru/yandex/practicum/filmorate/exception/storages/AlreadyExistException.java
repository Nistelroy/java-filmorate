package ru.yandex.practicum.filmorate.exception.storages;

public class AlreadyExistException extends StorageException {
    public AlreadyExistException(String message) {
        super(message);
    }
}