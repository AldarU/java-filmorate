package ru.yandex.practicum.filmorate.db.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("A like has been added");
    }

    public void dislike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id=? AND user_id=?",
                                filmId, userId);
        log.info("The like has been deleted");
    }

    public int getCountLikesInFilm(int filmId) {
        String sqlQuery = "SELECT COUNT(*) " +
                          "FROM likes " +
                          "WHERE film_id = ? ";
        log.info("The number of likes in the movie was received");
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
    }
}
