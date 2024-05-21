package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private int id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank
    @Size(max = 200, message = "Description должно иметь не более 200 символов")
    private String description;

    @PositiveOrZero(message = "Duration должно быть больше 0")
    private int duration;

    @Valid
    private Mpa mpa;

    private LocalDate releaseDate;

    @Valid
    private List<Genre> genres = new ArrayList<>();

    private Set<Integer> likes = new HashSet<>();
}
