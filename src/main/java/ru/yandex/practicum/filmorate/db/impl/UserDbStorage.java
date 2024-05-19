package ru.yandex.practicum.filmorate.db.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.db.dbinterfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component("mainUser")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users ";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser);
        return users;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);

        jdbcInsert.withTableName("users");
        jdbcInsert.usingGeneratedKeyColumns("user_id");

        Map<String, Object> params = new HashMap<>();
        params.put("user_name", user.getName());
        params.put("login", user.getLogin());
        params.put("email", user.getEmail());
        params.put("birthday", user.getBirthday());
        int id = jdbcInsert.executeAndReturnKey(params).intValue();
        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users " +
                          "SET user_name = ?, login = ?, email = ?, birthday = ? " +
                          "WHERE user_id = ?";

        jdbcTemplate.update(
                sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return userGetById(user.getId());
    }

    public User userGetById(int userId) {
        String sqlQuery = "SELECT user_id, user_name, login, email, birthday " +
                          "FROM users " +
                          "WHERE user_id = ? ";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String name = resultSet.getString("user_name");
        String login = resultSet.getString("login");
        LocalDate localDate = resultSet.getDate("birthday").toLocalDate();
        String email = resultSet.getString("email");

        return User.builder()
                .id(userId)
                .name(name)
                .login(login)
                .birthday(localDate)
                .email(email)
                .build();
    }
}
