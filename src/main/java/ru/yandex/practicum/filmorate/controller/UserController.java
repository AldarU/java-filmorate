package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.db.dbservice.UserDbService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private final UserDbService userService;

    @Autowired
    public UserController(UserDbService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.getMutualFriends(id, friendId);
    }
}
