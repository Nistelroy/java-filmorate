package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.service.ServiceException;
import ru.yandex.practicum.filmorate.exception.service.ValidationException;
import ru.yandex.practicum.filmorate.exception.storages.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.storages.NotFoundException;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponsExceptions handleStorageNotFoundException(final NotFoundException ex) {
        return new ResponsExceptions("Ресурс не найден", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponsExceptions handleValidationException(final ValidationException ex) {
        return new ResponsExceptions("Ошибка валидации", ex.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponsExceptions handleServiceException(final ServiceException ex) {
        return new ResponsExceptions("Ошибка Сервиса", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponsExceptions handleAlreadyExistException(final AlreadyExistException ex) {
        return new ResponsExceptions("Ошибка сервера", ex.getMessage());
    }
}