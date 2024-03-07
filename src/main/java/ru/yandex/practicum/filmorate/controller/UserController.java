package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserCreateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    private int id = 0;

    private void plusId() {
        this.id++;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws UserCreateException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя user пустое. Именем станет логин.");
        }

        if (validateUser(user)) {
            plusId();
            user.setId(id);

            users.put(id, user);
            log.info("User добавлен в UsersList");


            return user;
        } else {
            log.warn("User не прошел валидацию");
            throw new UserCreateException("Возникла ошибка при валидации в POST-method");
        }
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws UserCreateException {
        if (validateUser(user) && users.containsKey(user.getId())) {
            users.put(id, user);
            log.info("User был обновлен в UsersList");

            return user;
        } else {
            throw new UserCreateException("Возникла ошибка при валидации в PUT-method");
        }

    }

    private boolean validateUser(User user) {
        boolean userLoginIsEmpty = user.getLogin().isEmpty() || user.getLogin().isBlank();
        boolean userEmailIsCorrect = !user.getEmail().isEmpty() && user.getEmail().contains("@");
        boolean birthdayIsFuture = user.getBirthday().isAfter(LocalDate.now());

        return !userLoginIsEmpty && !birthdayIsFuture && userEmailIsCorrect;
    }
}
