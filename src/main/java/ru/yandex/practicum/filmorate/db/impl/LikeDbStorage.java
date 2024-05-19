package ru.yandex.practicum.filmorate.db.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void like(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                                filmId, userId);
    }

    public void dislike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?",
                                filmId, userId);
    }

    public int getCountLikesInFilm(int filmId) {
        String sqlQuery = "SELECT COUNT(*) " +
                          "FROM likes " +
                          "WHERE film_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
    }
}
