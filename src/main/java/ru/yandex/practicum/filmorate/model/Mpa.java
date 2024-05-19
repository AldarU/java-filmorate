package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Mpa {
    @Max(5)
    @Min(1)
    private int id;
    private String name;
}
