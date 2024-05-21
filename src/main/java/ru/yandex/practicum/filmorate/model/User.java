package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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

    @JsonIgnore
    Set<Integer> friendList = new HashSet<>();
}
