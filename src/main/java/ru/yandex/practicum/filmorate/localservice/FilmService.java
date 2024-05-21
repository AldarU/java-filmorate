package ru.yandex.practicum.filmorate.localservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FilmStorage;

import java.util.List;

@Service
public class FilmService { // сервис отвечающий за работу с лайками
    FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getFilm() {
        return inMemoryFilmStorage.getFilm();
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {  // добавление лайка
        inMemoryFilmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) { // удаление лайка
        inMemoryFilmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilms(int likeCount) { // вывода топа фильмов
        return inMemoryFilmStorage.getTopFilms(likeCount);
    }
}
