package ru.yandex.practicum.filmorate.db.dbservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.dbinterfaces.MpaDb;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmDbService {  // класс отвечающий за работу над film после получения его из БД
    private FilmStorage filmDbStorage;
    private MpaDb mpaDb;

    public FilmDbService(@Qualifier("main") FilmStorage filmDbStorage, MpaDb mpaDb) {
        this.filmDbStorage = filmDbStorage;
        this.mpaDb = mpaDb;
    }

    public List<Film> getFilm() {
        return filmDbStorage.getFilm();
    }

    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            Film actualFilm = filmDbStorage.addFilm(film);

            filmDbStorage.addGenres(actualFilm.getId(), film.getGenres());
            actualFilm.setGenres(filmDbStorage.getGenres(actualFilm.getId()));
            actualFilm.setMpa(mpaDb.getMpaById(actualFilm.getMpa().getId()));

            return actualFilm;
        } else {
            throw new ValidationException("Возникла ошибка при добавлении фильма");
        }
    }

    public Film updateFilm(Film film) {
        if (validateFilm(film) && isContains(film.getId())) {
            Film actualFilm = filmDbStorage.updateFilm(film);

            filmDbStorage.updateGenres(actualFilm.getId(), film.getGenres());
            actualFilm.setGenres(filmDbStorage.getGenres(actualFilm.getId()));
            actualFilm.setMpa(mpaDb.getMpaById(actualFilm.getMpa().getId()));

            return actualFilm;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не был найден");
        }
    }

    public Film getFilmById(int filmId) {
        Film film = filmDbStorage.getFilmById(filmId);

        film.setGenres(filmDbStorage.getGenres(filmId));
        film.setMpa(mpaDb.getMpaById(film.getMpa().getId()));

        return film;
    }

    public void addLike(int filmId, int userId) {
        if (isContains(filmId)) {
            filmDbStorage.addLike(filmId, userId);
        }
    }

    public void removeLike(int filmId, int userId) {
        if (isContains(filmId)) {
            filmDbStorage.removeLike(filmId, userId);
        }
    }

    public List<Film> getTopFilms(int likeCount) {
        return filmDbStorage.getTopFilms(likeCount);
    }

    public boolean isContains(int id) {  // проверка есть ли такой фильм
        try {
            filmDbStorage.getFilmById(id);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private boolean validateFilm(Film film) {  // проверяем подходит ли фильм под минимальный localdate
        LocalDate earlyFilmDate = LocalDate.of(1895, 12, 28);
        return film.getReleaseDate().isAfter(earlyFilmDate);
    }
}