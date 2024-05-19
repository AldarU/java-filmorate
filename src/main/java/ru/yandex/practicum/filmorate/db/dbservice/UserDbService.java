package ru.yandex.practicum.filmorate.db.dbservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.dbinterfaces.FriendsDb;
import ru.yandex.practicum.filmorate.db.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDbService {
    private UserDbStorage userDbStorage;
    private FriendsDb friendsDb;

    @Autowired
    public UserDbService(@Qualifier("mainUser") UserDbStorage userDbStorage, FriendsDb friendsDb) {
        this.userDbStorage = userDbStorage;
        this.friendsDb = friendsDb;
    }

    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (validateUser(user)) {
            return userDbStorage.addUser(user);
        } else {
            throw new ValidationException("Возникла ошибка при добавлении пользователя");
        }
    }

    public User updateUser(User user) {
        if (validateUser(user) && isContains(user.getId())) {
            return userDbStorage.updateUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не был найден");
        }
    }

    public User addFriend(int userId, int friendId) { // добавление друга
        boolean status = false;

        for (User friend : getFriends(friendId)) {
            if (friend.getId() == userId) {
                status = true;
            }
        }
        friendsDb.addFriend(userId, friendId, status);
        return userDbStorage.userGetById(userId);
    }

    public List<User> getFriends(int id) {  // ищем друзей
        if (isContains(id)) { // для начала проверяем есть ли вообще такой user в бд
            List<Friends> friendsList = friendsDb.getFriends(id);

            return friendsList.stream()
                    .map(x -> userDbStorage.userGetById(x.getFriendId()))
                    .collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не был найден");
        }
    }

    public void deleteFriend(int id, int friendId) {
        if (isContains(id) && isContains(friendId)) { // для начала проверяем есть ли вообще такой user в бд
            friendsDb.deleteFriend(id, friendId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "При удалении пользователь не был найден");
        }
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        Set<User> userSet = new HashSet<>(getFriends(userId));
        userSet.retainAll(getFriends(friendId));
        return new ArrayList<>(userSet);
    }

    public boolean isContains(int id) {  // проверка есть ли такой фильм
        try {
            userDbStorage.userGetById(id);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private boolean validateUser(User user) {
        boolean birthdayIsFuture = user.getBirthday().isAfter(LocalDate.now());
        return !birthdayIsFuture;
    }
}
