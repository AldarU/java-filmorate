package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank
    @Size(max = 200, message = "Description должно иметь не более 200 символов")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive(message = "Duration должно быть больше 0")
    private int duration;
}
