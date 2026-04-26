package pl.lodz.p.library.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.domain.model.*;

class ModelTest {

    @Test
    void readerConstructorTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", "Jan", "Kowalski", 25);
        Assertions.assertEquals("testUser", reader.getLogin());
        Assertions.assertEquals("test@gmail.com", reader.getEmail());
        Assertions.assertEquals("Jan", reader.getFirstName());
        Assertions.assertEquals("Kowalski", reader.getLastName());
        Assertions.assertEquals(25, reader.getAge());
        Assertions.assertFalse(reader.isActive());
    }

    @Test
    void administratorConstructorTest() {
        Administrator admin = new Administrator("adminUser", "admin@gmail.com", "Adam", "Administrator", 40);
        Assertions.assertEquals("adminUser", admin.getLogin());
        Assertions.assertEquals("admin@gmail.com", admin.getEmail());
        Assertions.assertEquals("Adam", admin.getFirstName());
        Assertions.assertEquals("Administrator", admin.getLastName());
        Assertions.assertEquals(40, admin.getAge());
        Assertions.assertFalse(admin.isActive());
    }

    @Test
    void librarianConstructorTest() {
        Librarian librarian = new Librarian("libUser", "lib@gmail.com", "Anna", "Bibliotekarz", 35);
        Assertions.assertEquals("libUser", librarian.getLogin());
        Assertions.assertEquals("lib@gmail.com", librarian.getEmail());
        Assertions.assertEquals("Anna", librarian.getFirstName());
        Assertions.assertEquals("Bibliotekarz", librarian.getLastName());
        Assertions.assertEquals(35, librarian.getAge());
        Assertions.assertFalse(librarian.isActive());
    }
}