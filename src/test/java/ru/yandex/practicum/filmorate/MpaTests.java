package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.db.dbservice.MpaDbService;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaTests {
    private final MpaDbService mpaDbService;

    @Test
    public void testGetMpa() {
        Assertions.assertEquals(mpaDbService.getMpa().size(), 5);
    }

    @Test
    public void testGetMpaById() {
        Assertions.assertEquals(mpaDbService.getMpaById(1).getName(), "G");
        Assertions.assertEquals(mpaDbService.getMpaById(2).getName(), "PG");
    }
}
