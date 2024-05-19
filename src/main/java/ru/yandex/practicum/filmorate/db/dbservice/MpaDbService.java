package ru.yandex.practicum.filmorate.db.dbservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.dbinterfaces.MpaDb;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaDbService {
    MpaDb mpaDbStorage;

    @Autowired
    public MpaDbService(MpaDb mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getMpa() {
        return mpaDbStorage.getMpa();
    }

    public Mpa getMpaById(int id) {
        if (id <= 5) {
            return mpaDbStorage.getMpaById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный MPA не найден");
        }
    }
}
