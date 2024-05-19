package ru.yandex.practicum.filmorate.db.dbinterfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDb {
    List<Mpa> getMpa();
    Mpa getMpaById(int id);
}
