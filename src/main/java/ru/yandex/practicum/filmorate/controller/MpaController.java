package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.db.dbservice.MpaDbService;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaDbService mpaDbService;

    @Autowired
    public MpaController(MpaDbService mpaDbService) {
        this.mpaDbService = mpaDbService;
    }

    @GetMapping()
    public List<Mpa> getMpa() {
        return mpaDbService.getMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        return mpaDbService.getMpaById(id);
    }
}
