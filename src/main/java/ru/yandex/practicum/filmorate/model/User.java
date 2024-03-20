package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @NotNull
    private LocalDate birthday;

    Set<Integer> friendList = new HashSet<>();
}
