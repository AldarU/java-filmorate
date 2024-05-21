package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.db.dbservice.GenreDbService;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreDbService genreDbService;

    @Autowired
    public GenreController(GenreDbService genreDbService) {
        this.genreDbService = genreDbService;
    }

    @GetMapping()
    public List<Genre> getGenres() {
        return genreDbService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return genreDbService.getGenreById(id);
    }
}
