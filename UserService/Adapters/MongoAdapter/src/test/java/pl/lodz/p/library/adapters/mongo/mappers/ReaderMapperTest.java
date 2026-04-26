package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.library.domain.model.Reader;

import static org.junit.jupiter.api.Assertions.*;

class ReaderMapperTest {

    private final ReaderMapper mapper = new ReaderMapper();

    @Test
    void toDomain() {
        ReaderDoc doc = new ReaderDoc("reader1", "pass", "read@test.pl", "Piotr", "Wiśniewski", 25, true);
        doc.setId("1");

        Reader reader = mapper.toDomain(doc);

        assertNotNull(reader);
        assertEquals("1", reader.getId());
        assertEquals("reader1", reader.getLogin());
        assertEquals("pass", reader.getPassword());
        assertEquals("read@test.pl", reader.getEmail());
        assertEquals("Piotr", reader.getFirstName());
        assertEquals("Wiśniewski", reader.getLastName());
        assertEquals(25, reader.getAge());
        assertTrue(reader.isActive());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        Reader reader = new Reader("reader1", "pass", "read@test.pl", "Piotr", "Wiśniewski", 25);
        reader.setId("1");
        reader.setActive(true);

        ReaderDoc doc = mapper.toDocument(reader);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
        assertEquals("reader1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("read@test.pl", doc.getEmail());
        assertEquals("Piotr", doc.getFirstName());
        assertEquals("Wiśniewski", doc.getLastName());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals("READER", doc.getAccessLevel());
    }
}