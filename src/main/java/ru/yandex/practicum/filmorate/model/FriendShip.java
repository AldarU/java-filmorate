package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendShip {
    private Long id;
    private Long friendId;
    private boolean isFriend;
}

