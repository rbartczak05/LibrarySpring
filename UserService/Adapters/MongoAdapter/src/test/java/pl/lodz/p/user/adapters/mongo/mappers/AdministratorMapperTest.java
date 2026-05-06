package pl.lodz.p.user.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.user.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.user.domain.model.Administrator;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorMapperTest {

    private final AdministratorMapper mapper = new AdministratorMapper();

    @Test
    void toDomain() {
        AdministratorDoc doc = new AdministratorDoc("admin1", "pass", "admin@test.pl", "Jan", "Kowalski", 30, true);
        UUID id = UUID.randomUUID();
        doc.setId(id);

        assertEquals(id, doc.getId());

        Administrator admin = mapper.toDomain(doc);

        assertNotNull(admin);
        assertEquals(id, admin.getId());
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
        UUID id = UUID.randomUUID();
        admin.setId(id);
        admin.setActive(true);

        assertEquals(id, admin.getId());

        AdministratorDoc doc = mapper.toDocument(admin);

        assertNotNull(doc);
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