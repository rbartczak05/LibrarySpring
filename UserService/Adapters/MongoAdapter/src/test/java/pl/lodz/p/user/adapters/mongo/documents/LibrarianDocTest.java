package pl.lodz.p.user.adapters.mongo.documents;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LibrarianDocTest {

    @Test
    void testLibrarianDoc() {
        LibrarianDoc doc = new LibrarianDoc("lib1", "pass", "lib@test.pl", "Anna", "Nowak", 40, false);
        UUID id = UUID.randomUUID();
        doc.setId(id);

        assertEquals(id, doc.getId());
        assertEquals("lib1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("lib@test.pl", doc.getEmail());
        assertEquals("Anna", doc.getFirstName());
        assertEquals("Nowak", doc.getLastName());
        assertEquals(40, doc.getAge());
        assertFalse(doc.isActive());
        assertEquals("LIBRARIAN", doc.getAccessLevel());
    }
}