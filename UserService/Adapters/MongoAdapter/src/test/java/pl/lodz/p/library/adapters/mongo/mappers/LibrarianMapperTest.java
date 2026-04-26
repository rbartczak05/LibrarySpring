package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.library.domain.model.Librarian;

import static org.junit.jupiter.api.Assertions.*;

class LibrarianMapperTest {

    private final LibrarianMapper mapper = new LibrarianMapper();

    @Test
    void toDomain() {
        LibrarianDoc doc = new LibrarianDoc("lib1", "pass", "lib@test.pl", "Anna", "Nowak", 40, true);
        doc.setId("1");

        Librarian lib = mapper.toDomain(doc);

        assertNotNull(lib);
        assertEquals("1", lib.getId());
        assertEquals("lib1", lib.getLogin());
        assertEquals("pass", lib.getPassword());
        assertEquals("lib@test.pl", lib.getEmail());
        assertEquals("Anna", lib.getFirstName());
        assertEquals("Nowak", lib.getLastName());
        assertEquals(40, lib.getAge());
        assertTrue(lib.isActive());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        Librarian lib = new Librarian("lib1", "pass", "lib@test.pl", "Anna", "Nowak", 40);
        lib.setId("1");
        lib.setActive(true);

        LibrarianDoc doc = mapper.toDocument(lib);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
        assertEquals("lib1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("lib@test.pl", doc.getEmail());
        assertEquals("Anna", doc.getFirstName());
        assertEquals("Nowak", doc.getLastName());
        assertEquals(40, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals("LIBRARIAN", doc.getAccessLevel());
    }
}