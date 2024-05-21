package ru.yandex.practicum.filmorate.db.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("main")
public class FilmDbStorage implements FilmStorage {  // класс отвечающий за запросы к БД
    private final JdbcTemplate jdbcTemplate;
    private LikeDbStorage likeDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.likeDbStorage = likeDbStorage;
    }

    @Override
    public List<Film> getFilm() {
        String sqlQuery = "SELECT * FROM films ";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        log.info("A list of movies is being received");
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);

        jdbcInsert.withTableName("films");
        jdbcInsert.usingGeneratedKeyColumns("film_id");

        Map<String, Object> params = new HashMap<>();
        params.put("film_name", film.getName());
        params.put("description", film.getDescription());
        params.put("duration", film.getDuration());
        params.put("release_date", film.getReleaseDate());
        params.put("rating_id", film.getMpa().getId());
        int id = jdbcInsert.executeAndReturnKey(params).intValue();
        film.setId(id);
        log.info("A movie has been added");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        log.info("The movie has been updated");

        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getTopFilms(int limit) { // вывод топ фильмов
        log.info("There was a receipt of the top films");
        return getFilm().stream()
                .sorted((f0, f1) -> likeDbStorage.getCountLikesInFilm(f1.getId()) - likeDbStorage.getCountLikesInFilm(f0.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void addGenres(int filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
        log.info("A genre has been added");
    }

    @Override
    public void updateGenres(int filmId, List<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id=?", filmId);
        for (Genre genre : genres) {
            jdbcTemplate.update(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
        }
        log.info("A genre has been updated");
    }

    @Override
    public List<Genre> getGenres(int filmId) {
        String sqlQuery = "SELECT f.genre_id, g.genre_name FROM film_genres AS f " +
                "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id " +
                "WHERE f.film_id= ? " +
                "ORDER BY g.genre_id";

        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId))
                .stream()
                .distinct()
                .collect(Collectors.toList());

        log.info("a list of genres was received");

        return genres;
    }

    @Override
    public void addLike(int filmId, int userId) {
        likeDbStorage.like(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        likeDbStorage.dislike(filmId, userId);
    }

    @Override
    public Film getFilmById(int filmId) {
        String sqlQuery = "SELECT film_id, film_name, description, duration, release_date, rating_id " +
                "FROM films " +
                "WHERE film_id = ?";

        log.info("The movie was received by id");

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException { // создание фильма
        int filmId = resultSet.getInt("film_id");
        String name = resultSet.getString("film_name");
        String description = resultSet.getString("description");
        int duration = resultSet.getInt("duration");
        LocalDate localDate = resultSet.getDate("release_date").toLocalDate();
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("rating_id"));

        return Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .duration(duration)
                .releaseDate(localDate)
                .mpa(mpa)
                .build();
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException { // создание жанра
        int genreId = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");

        return Genre.builder()
                .id(genreId)
                .name(name)
                .build();
    }
}
