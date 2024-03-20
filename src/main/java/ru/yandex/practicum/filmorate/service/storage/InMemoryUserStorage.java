package ru.yandex.practicum.filmorate.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundFilmorateExceptions;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class InMemoryUserStorage implements UserStorage {
    protected static Map<Integer, User> users = new HashMap<>();

    private int id = 0;

    private void plusId() {
        this.id++;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
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
            if (!validateUser(user)) {
                throw new ValidationException("Возникла ошибка при валидации в POST-method");
            } else {
                throw new NotFoundFilmorateExceptions("Фильм не найден");
            }
        }
    }

    @Override
    public User updateUser(User user) {
        if (validateUser(user) && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("User был обновлен в UsersList");
            return user;
        } else {
            if (!validateUser(user)) {
                throw new ValidationException("Возникла ошибка при валидации в PUT-method");
            } else {
                throw new NotFoundFilmorateExceptions("Фильм не найден");
            }
        }
    }

    private boolean validateUser(User user) {
        boolean birthdayIsFuture = user.getBirthday().isAfter(LocalDate.now());
        return !birthdayIsFuture;
    }
}
