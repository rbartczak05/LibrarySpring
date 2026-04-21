package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookSetDocTest {

    @Test
    void testBookSetDoc() {
        BookSetDoc doc = new BookSetDoc("Title", "Author", 2020, 5);
        doc.setId("123");

        assertEquals("123", doc.getId());
        assertEquals("Title", doc.getTitle());
        assertEquals("Author", doc.getAuthor());
        assertEquals(2020, doc.getReleaseYear());
        assertEquals(5, doc.getQuantity());
    }
}