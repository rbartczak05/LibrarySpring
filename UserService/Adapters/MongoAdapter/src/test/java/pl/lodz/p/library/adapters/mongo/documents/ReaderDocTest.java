package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReaderDocTest {

    @Test
    void testReaderDoc() {
        ReaderDoc doc = new ReaderDoc("reader1", "pass", "reader@test.pl", 25);
        doc.setId("123");
        doc.setActive(true);
        doc.setCurrentLoansCount(3);

        assertEquals("123", doc.getId());
        assertEquals("reader1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("reader@test.pl", doc.getEmail());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals(3, doc.getCurrentLoansCount());
    }
}