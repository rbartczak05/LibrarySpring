package pl.lodz.p.user.adapters.mongo.documents;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdministratorDocTest {

    @Test
    void testAdministratorDoc() {
        AdministratorDoc doc = new AdministratorDoc("admin1", "pass", "admin@test.pl", "Jan", "Kowalski", 30, true);
        UUID id = UUID.randomUUID();
        doc.setId(id);

        assertEquals(id, doc.getId());
        assertEquals("admin1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("admin@test.pl", doc.getEmail());
        assertEquals("Jan", doc.getFirstName());
        assertEquals("Kowalski", doc.getLastName());
        assertEquals(30, doc.getAge());
        assertTrue(doc.isActive());
        assertEquals("ADMINISTRATOR", doc.getAccessLevel());
    }
}