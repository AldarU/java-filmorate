package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundFilmorateExceptions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService { // сервис отвечающий за работу с лайками
    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
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

    public Film addLike(int filmId, int userId) {  // добавление лайка
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);

        return film;
    }

    public Film removeLike(int filmId, int userId) { // удаление лайка
        Film film = getFilmById(filmId);

        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            throw new NotFoundFilmorateExceptions("userId " + userId);
        }

        return film;
    }

    public List<Film> getTopFilms(int likeCount) { // вывода топа фильмов
        List<Film> films = inMemoryFilmStorage.getFilm();

        films = films.stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(likeCount > 0 ? 10 : likeCount)
                .collect(Collectors.toList());

        return films;
    }

    private Film getFilmById(int id) { // поиск фильма по айди
        Film film = inMemoryFilmStorage.getFilm().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .get();

        return film;
    }
}
