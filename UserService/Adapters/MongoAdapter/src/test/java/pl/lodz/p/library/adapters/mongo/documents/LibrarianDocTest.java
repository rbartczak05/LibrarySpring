package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibrarianDocTest {

    @Test
    void testLibrarianDoc() {
        LibrarianDoc doc = new LibrarianDoc("lib1", "pass", "lib@test.pl", 40);
        doc.setId("123");
        doc.setActive(false);

        assertEquals("123", doc.getId());
        assertEquals("lib1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("lib@test.pl", doc.getEmail());
        assertEquals(40, doc.getAge());
        assertFalse(doc.isActive());
    }
}