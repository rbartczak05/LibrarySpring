package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdministratorDocTest {

    @Test
    void testAdministratorDoc() {
        AdministratorDoc doc = new AdministratorDoc("admin1", "pass", "admin@test.pl", 30);
        doc.setId("123");
        doc.setActive(true);

        assertEquals("123", doc.getId());
        assertEquals("admin1", doc.getLogin());
        assertEquals("pass", doc.getPassword());
        assertEquals("admin@test.pl", doc.getEmail());
        assertEquals(30, doc.getAge());
        assertTrue(doc.isActive());
    }
}