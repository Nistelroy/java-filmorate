package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Mpa {
    @NotNull
    private final int id;
    @NotNull
    private final String name;
    private final String description;
}
