package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookSetDocTest {

    @Test
    void testBookSetDoc() {
        UUID id = UUID.randomUUID();
        BookSetDoc doc = new BookSetDoc("Title", "Author", 2020, 5);
        doc.setId(id);

        assertEquals(id, doc.getId());
        assertEquals("Title", doc.getTitle());
        assertEquals("Author", doc.getAuthor());
        assertEquals(2020, doc.getReleaseYear());
        assertEquals(5, doc.getQuantity());
    }
}