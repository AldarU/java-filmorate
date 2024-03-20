package ru.yandex.practicum.filmorate.service.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilm();
    Film addFilm(Film film);
    Film updateFilm(Film film);
}
