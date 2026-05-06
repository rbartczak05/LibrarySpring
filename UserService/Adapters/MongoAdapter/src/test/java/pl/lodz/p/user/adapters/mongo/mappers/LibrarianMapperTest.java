package pl.lodz.p.user.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.user.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.user.domain.model.Librarian;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LibrarianMapperTest {

    private final LibrarianMapper mapper = new LibrarianMapper();

    @Test
    void toDomain() {
        LibrarianDoc doc = new LibrarianDoc("lib1", "pass", "lib@test.pl", "Anna", "Nowak", 40, true);
        UUID id = UUID.randomUUID();
        doc.setId(id);

        Librarian lib = mapper.toDomain(doc);

        assertNotNull(lib);
        assertEquals(id, lib.getId());
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
        UUID id = UUID.randomUUID();
        lib.setId(id);
        lib.setActive(true);

        LibrarianDoc doc = mapper.toDocument(lib);

        assertNotNull(doc);
        assertEquals(id, doc.getId());
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