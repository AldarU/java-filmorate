package ru.yandex.practicum.filmorate.db.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dbinterfaces.GenresDb;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenresDb {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "SELECT genre_id, genre_name " +
                "FROM genres ";
        log.info("A list of genres was received");
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT genre_id, genre_name " +
                "FROM genres " +
                "WHERE genre_id=?";

        Genre genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        log.info("The genre was obtained by id");
        return genre;
    }

    public Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int genreId = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");

        return Genre.builder()
                .id(genreId)
                .name(name)
                .build();
    }
}
