package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.library.domain.model.Administrator;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorMapperTest {

    private final AdministratorMapper mapper = new AdministratorMapper();

    @Test
    void toDomain() {
        AdministratorDoc doc = new AdministratorDoc("admin1", "pass", "admin@test.pl", "Jan", "Kowalski", 30, true);
        doc.setId("1");

        Administrator admin = mapper.toDomain(doc);

        assertNotNull(admin);
        assertEquals("1", admin.getId());
        assertEquals("admin1", admin.getLogin());
        assertEquals("pass", admin.getPassword());
        assertEquals("admin@test.pl", admin.getEmail());
        assertEquals("Jan", admin.getFirstName());
        assertEquals("Kowalski", admin.getLastName());
        assertEquals(30, admin.getAge());
        assertTrue(admin.isActive());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        Administrator admin = new Administrator("admin1", "pass", "admin@test.pl", "Jan", "Kowalski", 30);
        admin.setId("1");
        admin.setActive(true);

        AdministratorDoc doc = mapper.toDocument(admin);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
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