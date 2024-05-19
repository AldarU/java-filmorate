package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.db.dbservice.MpaDbService;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
public class MpaController {
    private final MpaDbService mpaDbService;

    @Autowired
    public MpaController(MpaDbService mpaDbService) {
        this.mpaDbService = mpaDbService;
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpa() {
        return mpaDbService.getMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        return mpaDbService.getMpaById(id);
    }
}
