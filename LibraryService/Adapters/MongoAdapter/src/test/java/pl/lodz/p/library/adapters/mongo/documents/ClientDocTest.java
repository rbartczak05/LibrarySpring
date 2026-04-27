package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientDocTest {

    @Test
    void testClientDoc() {
        ClientDoc doc = new ClientDoc("Jan", "Kowalski", "jan@test.pl", 25);
        doc.setId("123");
        doc.setActive(true);
        doc.setCurrentLoansCount(3);

        assertEquals("123", doc.getId());
        assertEquals("Jan", doc.getFirstName());
        assertEquals("Kowalski", doc.getLastName());
        assertEquals("jan@test.pl", doc.getEmail());
        assertEquals(25, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals(3, doc.getCurrentLoansCount());
    }
}