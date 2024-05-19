package ru.yandex.practicum.filmorate.db.dbservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.db.dbinterfaces.GenresDb;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreDbService {
    GenresDb mpaGenres;

    @Autowired
    public GenreDbService(GenresDb mpaGenres) {
        this.mpaGenres = mpaGenres;
    }

    public List<Genre> getGenres() {
        return mpaGenres.getGenres();
    }

    public Genre getGenreById(int id) {
        if (id <= 6) {
            return mpaGenres.getGenreById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный GENRE не найден");
        }
    }
}
