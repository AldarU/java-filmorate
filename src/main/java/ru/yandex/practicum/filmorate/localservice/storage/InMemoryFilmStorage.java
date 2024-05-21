package ru.yandex.practicum.filmorate.localservice.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FilmStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundFilmorateExceptions;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int id;

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
            throw new ValidationException("Возникла ошибка при валидации в POST-method");
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

    @Override
    public List<Film> getTopFilms(int likeCount) { // вывода топа фильмов
        List<Film> films = getFilm();

        films = films.stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(likeCount > 0 ? likeCount : 10)
                .collect(Collectors.toList());

        return films;
    }

    @Override
    public void addGenres(int filmId, List<Genre> genres) {

    }

    @Override
    public void updateGenres(int filmId, List<Genre> genres) {

    }

    @Override
    public List<Genre> getGenres(int filmId) {
        return null;
    }


    public void addLike(int filmId, int userId) {  // добавление лайка
        Film film = getFilmById(filmId);

        if (!film.getLikes().contains(userId)) {
            film.getLikes().add(userId);
        } else {
            throw new ValidationException("Некорректный запрос. Нельзя дважды поставить лайк.");
        }
    }

    public void removeLike(int filmId, int userId) { // удаление лайка
        Film film = getFilmById(filmId);

        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            throw new NotFoundFilmorateExceptions("userId " + userId);
        }
    }

    public Film getFilmById(int id) { // поиск фильма по айди
        Film film = getFilm().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .get();

        return film;
    }

    private boolean validateFilm(Film film) {
        LocalDate earlyFilmDate = LocalDate.of(1895, 12, 28);
        return film.getReleaseDate().isAfter(earlyFilmDate);
    }
}
