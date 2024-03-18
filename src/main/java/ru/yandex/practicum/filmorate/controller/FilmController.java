package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmCreateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();

    private int id = 0;

    private void plusId() {
        this.id++;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws FilmCreateException {
        if (validateFilm(film)) {
            plusId();
            film.setId(id);

            films.put(id, film);
            log.info("Film добавлен в FilmsList");

            return film;
        } else {
            log.warn("Film не прошел валидацию");
            throw new FilmCreateException("Возникла ошибка при валидации в POST-method");
        }
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws FilmCreateException {
        if (validateFilm(film) && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Film был обновлен");
            return film;
        } else {
            log.warn("Film не прошел валидацию");
            throw new FilmCreateException("Возникла ошибка при валидации в PUT-method"); // перепроверь конструктор
        }
    }

    private boolean validateFilm(Film film) {
        LocalDate earlyFilmDate = LocalDate.of(1895, 12, 28);
        return film.getReleaseDate().isAfter(earlyFilmDate);
    }
}
