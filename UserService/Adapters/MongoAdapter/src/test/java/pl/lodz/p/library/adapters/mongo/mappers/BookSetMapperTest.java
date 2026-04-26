package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.domain.model.BookSet;

import static org.junit.jupiter.api.Assertions.*;

class BookSetMapperTest {

    private final BookSetMapper mapper = new BookSetMapper();

    @Test
    void toDomain() {
        BookSetDoc doc = new BookSetDoc("Title", "Author", 2020, 5);
        doc.setId("1");

        BookSet domain = mapper.toDomain(doc);

        assertNotNull(domain);
        assertEquals("1", domain.getId());
        assertEquals("Title", domain.getTitle());
        assertEquals("Author", domain.getAuthor());
        assertEquals(2020, domain.getReleaseYear());
        assertEquals(5, domain.getQuantity());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        BookSet domain = new BookSet("Title", "Author", 2020, 5);
        domain.setId("1");

        BookSetDoc doc = mapper.toDocument(domain);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
        assertEquals("Title", doc.getTitle());
        assertEquals("Author", doc.getAuthor());
        assertEquals(2020, doc.getReleaseYear());
        assertEquals(5, doc.getQuantity());
    }

    @Test
    void toDocumentNull() {
        assertNull(mapper.toDocument(null));
    }
}