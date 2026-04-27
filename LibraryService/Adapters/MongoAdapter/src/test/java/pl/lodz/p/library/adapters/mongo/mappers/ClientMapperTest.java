package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;
import pl.lodz.p.library.domain.model.Client;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private final ClientMapper mapper = new ClientMapper();

    @Test
    void toDomain() {
        ClientDoc doc = new ClientDoc("Jan", "Kowalski", "jan@test.pl", 25);
        doc.setId("1");
        doc.setActive(true);
        doc.setCurrentLoansCount(2);

        Client client = mapper.toDomain(doc);

        assertNotNull(client);
        assertEquals("1", client.getId());
        assertEquals("Jan", client.getFirstName());
        assertEquals("Kowalski", client.getLastName());
        assertEquals("jan@test.pl", client.getEmail());
        assertEquals(25, client.getAge());
        assertTrue(client.isActive());
        assertEquals(2, client.getCurrentLoansCount());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        Client client = new Client("Jan", "Kowalski", "jan@test.pl", 25);
        client.setId("1");
        client.setActive(true);
        client.setCurrentLoansCount(2);

        ClientDoc doc = mapper.toDocument(client);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
        assertEquals("Jan", doc.getFirstName());
        assertEquals("Kowalski", doc.getLastName());
        assertEquals("jan@test.pl", doc.getEmail());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals(2, doc.getCurrentLoansCount());
    }

    @Test
    void toDocumentNull() {
        assertNull(mapper.toDocument(null));
    }
}