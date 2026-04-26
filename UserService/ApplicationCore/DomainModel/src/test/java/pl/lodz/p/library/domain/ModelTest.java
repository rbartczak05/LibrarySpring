package pl.lodz.p.library.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.domain.exceptions.UserException;
import pl.lodz.p.library.domain.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

class ModelTest {

    @Test
    void readerConstructorTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        Assertions.assertEquals("testUser", reader.getLogin());
        Assertions.assertEquals("test@gmail.com", reader.getEmail());
        Assertions.assertEquals(25, reader.getAge());
        Assertions.assertFalse(reader.isActive());
    }

    @Test
    void administratorConstructorTest() {
        Administrator admin = new Administrator("adminUser", "admin@gmail.com", 40);
        Assertions.assertEquals("adminUser", admin.getLogin());
        Assertions.assertEquals("admin@gmail.com", admin.getEmail());
        Assertions.assertEquals(40, admin.getAge());
        Assertions.assertTrue(admin.isActive());
    }

    @Test
    void librarianConstructorTest() {
        Librarian librarian = new Librarian("libUser", "lib@gmail.com", 35);
        Assertions.assertEquals("libUser", librarian.getLogin());
        Assertions.assertEquals("lib@gmail.com", librarian.getEmail());
        Assertions.assertEquals(35, librarian.getAge());
        Assertions.assertTrue(librarian.isActive());
    }
}