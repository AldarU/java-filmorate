package ru.yandex.practicum.filmorate.db.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.db.dbinterfaces.MpaDb;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaDb {  // добавь интерфейс
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpa() {
        String sqlQuery = "SELECT rating_id, rating_name " +
                "FROM rating_mpa ";
        log.info("The mpa was received");
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT rating_id, rating_name " +
                "FROM rating_mpa " +
                "WHERE rating_id=?";

        Mpa mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        log.info("The mpa was obtained by Id");

        return mpa;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int ratingId = resultSet.getInt("rating_id");
        String name = resultSet.getString("rating_name");

        return Mpa.builder()
                .id(ratingId)
                .name(name)
                .build();
    }
}
