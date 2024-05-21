package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.dbservice.GenreDbService;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreTests {
    private final GenreDbService genreDbService;

    @Test
    public void testGetGenre() {
        Assertions.assertEquals(genreDbService.getGenres().size(), 6);
    }

    @Test
    public void testGetGenreById() {
        Assertions.assertEquals(genreDbService.getGenreById(1).getName(), "Комедия");
        Assertions.assertEquals(genreDbService.getGenreById(2).getName(), "Драма");
    }
}
