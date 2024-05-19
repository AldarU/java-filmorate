package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.db.dbservice.UserDbService;
import ru.yandex.practicum.filmorate.model.User;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserTests {
    private final UserDbService userStorage;
    private final JdbcTemplate jdbcTemplate;

    private final User user = new User(1, "sssss@ya.ru", "ssssss",
            "SSSSSS", LocalDate.of(1997, 3, 3), new HashSet<>());
    private final User upUser = new User(2, "fffffff@ya.ru", "fffffff",
            "FFFFFF", LocalDate.of(1996, 5, 15), new HashSet<>());
    private final User mutualFriend = new User(3, "ffffffssss@ya.ru", "xxxxx",
            "xxxxxx", LocalDate.of(1986, 5, 15), new HashSet<>());

    @Test
    public void TestGetUsers() {
        userStorage.addUser(user);
        userStorage.addUser(upUser);
        Assertions.assertTrue(userStorage.getUsers().size() == 2);
    }

    @Test
    public void testAddUser() {
        userStorage.addUser(user);
        Assertions.assertFalse(userStorage.getUsers().isEmpty());
    }

    @Test
    public void testUpdateUser() {
        userStorage.addUser(user);
        user.setName("UPDATED_NAME");
        user.setLogin("UPDATED_LOGIN");
        userStorage.updateUser(user);
        Assertions.assertEquals("UPDATED_NAME", userStorage.getUsers().get(0).getName());
        Assertions.assertEquals("UPDATED_LOGIN", userStorage.getUsers().get(0).getLogin());
    }

    @Test
    public void testGetAndAddFriend() {
        User user1 = userStorage.addUser(user);
        User user2 = userStorage.addUser(upUser);

        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user1.getId());

        Assertions.assertFalse(userStorage.getFriends(user.getId()).isEmpty());
    }

    @Test
    public void testGetMutualFriends() {
        User user1 = userStorage.addUser(user);
        User user2 = userStorage.addUser(upUser);
        User mutual = userStorage.addUser(mutualFriend);

        userStorage.addFriend(user1.getId(), mutual.getId());
        userStorage.addFriend(user2.getId(), mutual.getId());

        Assertions.assertFalse(userStorage.getMutualFriends(user1.getId(), user2.getId()).isEmpty());
    }

    @Test
    public void testIsContains() {
        User user1 = userStorage.addUser(user);
        Assertions.assertTrue(userStorage.isContains(user1.getId()));
    }
}