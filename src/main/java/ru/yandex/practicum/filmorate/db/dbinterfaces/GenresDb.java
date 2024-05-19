package ru.yandex.practicum.filmorate.db.dbinterfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDb {
    List<Genre> getGenres();

    Genre getGenreById(int id);
}
