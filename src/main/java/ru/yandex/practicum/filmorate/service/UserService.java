package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    public User addFriend(int userId, int friendId) { // добавление друга
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.getFriendList().add(friendId);
        friend.getFriendList().add(userId);

        return user;
    }

    public User removeFriend(int userId, int friendId) { // удаление друга
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.getFriendList().remove(friendId);
        friend.getFriendList().remove(userId);

        return user;
    }

    public List<User> getAllFriend(int userId) {  // поиск друзей юзера
        User user = getUserById(userId);
        List<User> friendList = user.getFriendList()
                .stream()
                .map(id -> getUserById(id))
                .collect(Collectors.toList());

        return friendList;
    }

    public List<User> getMutualFriends(int userId, int friendId) {  // поиск общих друзей
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        Set<Integer> userSet = new HashSet<>(user.getFriendList());
        userSet.retainAll(friend.getFriendList());

        List<User> friendList = userSet
                .stream()
                .map(id -> getUserById(id))
                .collect(Collectors.toList());

        return friendList;
    }

    private User getUserById(int id) { // получение юзера по айди
        return inMemoryUserStorage.getUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .get();
    }
}
