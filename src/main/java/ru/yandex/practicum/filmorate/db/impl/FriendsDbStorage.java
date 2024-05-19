package ru.yandex.practicum.filmorate.db.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FriendsDb;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class FriendsDbStorage implements FriendsDb {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addFriend(int userId, int friendId, boolean isMutualFriends) {
        return jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)",
                userId, friendId, isMutualFriends);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "DELETE FROM friends " +
                          "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<Friends> getFriends(int id) {
        String sqlQuery = "SELECT user_id, friend_id, status " +
                "FROM friends " +
                "WHERE user_id = ? ";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFriends, id);
    }

    private Friends mapRowToFriends(ResultSet resultSet, int rowNum) throws SQLException {
        int userId = resultSet.getInt("user_id");
        int friendId = resultSet.getInt("friend_id");
        boolean isMutualFriends = resultSet.getBoolean("status");

        return Friends.builder()
                .userId(userId)
                .friendId(friendId)
                .status(isMutualFriends)
                .build();
    }
}
