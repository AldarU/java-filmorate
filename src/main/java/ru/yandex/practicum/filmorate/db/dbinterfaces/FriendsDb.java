package ru.yandex.practicum.filmorate.db.dbinterfaces;

import ru.yandex.practicum.filmorate.model.Friends;

import java.util.List;

public interface FriendsDb {
    Integer addFriend(int id, int friendId, boolean isMutualFriends);

    void deleteFriend(int id, int friendId);

    List<Friends> getFriends(int id);

    List<Friends> getFriendById(int userId, int friendId);
}
