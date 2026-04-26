package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReaderDocTest {

    @Test
    void testReaderDoc() {
        ReaderDoc doc = new ReaderDoc("reader1", "pass", "reader@test.pl", "Piotr", "Wiśniewski", 25, true);
        doc.setId("123");

        assertEquals("123", doc.getId());
        assertEquals("reader1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("reader@test.pl", doc.getEmail());
        assertEquals("Piotr", doc.getFirstName());
        assertEquals("Wiśniewski", doc.getLastName());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals("READER", doc.getAccessLevel());
    }
}