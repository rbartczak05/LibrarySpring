package pl.lodz.p.library.adapters.mongo.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
class ClientRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.2.7");

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        ClientDoc client1 = new ClientDoc("Jan", "Kowalski", "email1@test.pl", 20);
        client1.setId(UUID.randomUUID());
        client1.setActive(true);
        clientRepository.save(client1);

        ClientDoc client2 = new ClientDoc("Anna", "Nowak", "email2@test.pl", 30);
        client2.setId(UUID.randomUUID());
        client2.setActive(false);
        clientRepository.save(client2);
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    void findByEmail() {
        List<ClientDoc> clients = clientRepository.findByEmail("email2@test.pl");
        assertEquals(1, clients.size());
        assertEquals("email2@test.pl", clients.get(0).getEmail());
    }
}