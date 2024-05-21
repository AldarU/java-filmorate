package ru.yandex.practicum.filmorate.db.dbinterfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilm();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getTopFilms(int likeCount);

    void addGenres(int filmId, List<Genre> genres);

    void updateGenres(int filmId, List<Genre> genres);

    List<Genre> getGenres(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int id, int userId);

    Film getFilmById(int filmId);
}
