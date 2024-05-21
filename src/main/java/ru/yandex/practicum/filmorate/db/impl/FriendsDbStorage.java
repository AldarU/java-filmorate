package ru.yandex.practicum.filmorate.db.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FriendsDb;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class FriendsDbStorage implements FriendsDb {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer addFriend(int userId, int friendId, boolean isMutualFriends) {
        log.info("A friend has been added");
        return jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)",
                userId, friendId, isMutualFriends);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sqlQuery = "DELETE FROM friends " +
                          "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.info("The friend has been deleted");
    }

    @Override
    public List<Friends> getFriends(int id) {
        String sqlQuery = "SELECT user_id, friend_id, status " +
                "FROM friends " +
                "WHERE user_id = ? ";
        log.info("A list of friends was received");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriends, id);
    }

    @Override
    public List<Friends> getFriendById(int friendId, int userId) {
        String sqlQuery = "SELECT user_id, friend_id, status " +
                "FROM friends " +
                "WHERE friend_id = ? AND user_id = ? ";
        log.info("A list of mutual friends by id was received");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriends, friendId, userId);
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
