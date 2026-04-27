package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;
import pl.lodz.p.library.domain.model.Reader;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private final ClientMapper mapper = new ClientMapper();

    @Test
    void toDomain() {
        ClientDoc doc = new ClientDoc("reader1", "pass", "read@test.pl", 25);
        doc.setId("1");
        doc.setActive(true);
        doc.setCurrentLoansCount(2);

        Reader reader = mapper.toDomain(doc);

        assertNotNull(reader);
        assertEquals("1", reader.getId());
        assertEquals("reader1", reader.getLogin());
        assertEquals("pass", reader.getPassword());
        assertEquals("read@test.pl", reader.getEmail());
        assertEquals(25, reader.getAge());
        assertTrue(reader.isActive());
        assertEquals(2, reader.getCurrentLoansCount());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        Reader reader = new Reader("reader1", "pass", "read@test.pl", 25);
        reader.setId("1");
        reader.setActive(true);
        reader.setCurrentLoansCount(2);

        ClientDoc doc = mapper.toDocument(reader);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
        assertEquals("reader1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("read@test.pl", doc.getEmail());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals(2, doc.getCurrentLoansCount());
    }

    @Test
    void toDocumentNull() {
        assertNull(mapper.toDocument(null));
    }
}