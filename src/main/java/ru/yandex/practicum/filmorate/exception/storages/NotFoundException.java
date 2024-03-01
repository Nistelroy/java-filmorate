package ru.yandex.practicum.filmorate.exception.storages;


public class NotFoundException extends StorageException {
    public NotFoundException(String message) {
        super(message);
    }
}
