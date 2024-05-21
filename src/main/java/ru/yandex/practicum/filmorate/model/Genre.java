package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
public class Genre {
    @Min(1)
    @Max(6)
    private int id;
    private String name;
}
