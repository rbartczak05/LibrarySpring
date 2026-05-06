package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.domain.model.BookSet;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookSetMapperTest {

    private final BookSetMapper mapper = new BookSetMapper();

    @Test
    void toDomain() {
        UUID id = UUID.randomUUID();
        BookSetDoc doc = new BookSetDoc("Title", "Author", 2020, 5);
        doc.setId(id);

        BookSet domain = mapper.toDomain(doc);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
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
        UUID id = UUID.randomUUID();
        BookSet domain = new BookSet("Title", "Author", 2020, 5);
        domain.setId(id);

        BookSetDoc doc = mapper.toDocument(domain);

        assertNotNull(doc);
        assertEquals(id, doc.getId());
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