package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientDocTest {

    @Test
    void testClientDoc() {
        UUID id = UUID.randomUUID();
        ClientDoc doc = new ClientDoc("Jan", "Kowalski", "jan@test.pl", 25);
        doc.setId(id);
        doc.setActive(true);
        doc.setCurrentLoansCount(3);

        assertEquals(id, doc.getId());
        assertEquals("Jan", doc.getFirstName());
        assertEquals("Kowalski", doc.getLastName());
        assertEquals("jan@test.pl", doc.getEmail());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals(3, doc.getCurrentLoansCount());
    }
}