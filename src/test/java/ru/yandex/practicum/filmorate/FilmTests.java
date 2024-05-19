package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.db.dbservice.FilmDbService;
import ru.yandex.practicum.filmorate.db.dbservice.UserDbService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmTests {
    private final FilmDbService filmService;
    private final UserDbService userDbService;
    private final JdbcTemplate jdbcTemplate;

    private final Film film = new Film(
            1, "ss", "ddd", 1, new Mpa(1, ""), LocalDate.now(), new ArrayList<>(), new HashSet<>()
    );

    private final Film film2 = new Film(
            film.getId(), "ff", "df", 1, new Mpa(1, ""), LocalDate.now(), new ArrayList<>(), new HashSet<>()
    );

    private final User user = new User(1, "ss@mail.ru", "ddd", "dddd", LocalDate.now(), new HashSet<>(1));

    @AfterEach
    public void removeDb() {
        jdbcTemplate.execute("DELETE FROM likes");
        jdbcTemplate.execute("DELETE FROM films");
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    public void testAddFilms() {
        filmService.addFilm(film);
        Assertions.assertFalse(filmService.getFilm().isEmpty());
    }

    @Test
    public void testGetFilms() {
        Assertions.assertTrue(filmService.getFilm().isEmpty());
        filmService.addFilm(film);
        Assertions.assertFalse(filmService.getFilm().isEmpty());
    }

    @Test
    public void testUpdateFilms() {
        filmService.addFilm(film);

        film.setName("UPDATED");
        filmService.updateFilm(film);

        Film updFilm = filmService.getFilmById(film.getId());
        Assertions.assertEquals("UPDATED", updFilm.getName());
    }

    @Test
    public void testGetFilmById() {
        filmService.addFilm(film);
        Film copy = filmService.getFilmById(film.getId());
        Assertions.assertTrue(film.getId() == copy.getId());
    }

    @Test
    public void testAddLike() {
        filmService.addFilm(film);
        userDbService.addUser(user);
        filmService.addLike(film.getId(), user.getId());
    }

    @Test
    public void testRemoveLike() {
        filmService.addFilm(film);
        userDbService.addUser(user);
        filmService.addLike(film.getId(), user.getId());
        filmService.removeLike(film.getId(), user.getId());
    }

    @Test
    public void testGetTopFilms() {
        filmService.addFilm(film);
        filmService.addFilm(film2);
        userDbService.addUser(user);

        Assertions.assertEquals(filmService.getTopFilms(5).get(0).getId(), film.getId());
        Assertions.assertEquals(filmService.getTopFilms(5).get(1).getId(), film2.getId());
    }

    @Test
    public void testIsContains() {
        filmService.addFilm(film);
        Assertions.assertTrue(filmService.isContains(film.getId()));
    }
}
