package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.dbservice.FilmDbService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final FilmDbService filmService;

    @Autowired
    public FilmController(FilmDbService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilm();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam int count) {
        return filmService.getTopFilms(count);
    }
}
