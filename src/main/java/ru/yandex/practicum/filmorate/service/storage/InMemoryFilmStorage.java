package ru.yandex.practicum.filmorate.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundFilmorateExceptions;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 0;

    private void plusId() {
        this.id++;
    }

    @Override
    public List<Film> getFilm() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            plusId();
            film.setId(id);

            films.put(id, film);
            log.info("Film добавлен в FilmsList");

            return film;
        } else {
            log.warn("Film не прошел валидацию");
            if (!validateFilm(film)) {
                throw new ValidationException("Возникла ошибка при валидации в POST-method");
            } else {
                throw new NotFoundFilmorateExceptions("Фильм не найден");
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (validateFilm(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Film был обновлен");
            return film;
        } else {
            log.warn("Film не прошел валидацию");
            if (!validateFilm(film)) {
                throw new ValidationException("Возникла ошибка при валидации в PUT-method");
            } else {
                throw new NotFoundFilmorateExceptions("Фильм не найден");
            }
        }
    }

    private boolean validateFilm(Film film) {
        LocalDate earlyFilmDate = LocalDate.of(1895, 12, 28);
        return film.getReleaseDate().isAfter(earlyFilmDate);
    }

}
