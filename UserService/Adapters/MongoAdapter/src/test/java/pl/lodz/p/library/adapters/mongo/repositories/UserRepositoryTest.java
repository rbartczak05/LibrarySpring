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
import pl.lodz.p.library.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.library.adapters.mongo.documents.UserDoc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Testcontainers
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ReaderDoc user1 = new ReaderDoc("login1", "pass", "email1@test.pl", "Jan", "Kowalski", 20, true);
        userRepository.save(user1);

        ReaderDoc user2 = new ReaderDoc("log_other", "pass", "email2@test.pl", "Piotr", "Nowak", 30, false);
        userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findUserByLogin() {
        Optional<UserDoc> user = userRepository.findUserByLogin("login1");
        assertTrue(user.isPresent());
        assertEquals("login1", user.get().getLogin());
    }

    @Test
    void findUserByEmail() {
        Optional<UserDoc> user = userRepository.findUserByEmail("email2@test.pl");
        assertTrue(user.isPresent());
        assertEquals("email2@test.pl", user.get().getEmail());
    }

    @Test
    void findUsersByAge() {
        List<UserDoc> users = userRepository.findUsersByAge(20);
        assertEquals(1, users.size());
    }

    @Test
    void findUsersByActive() {
        List<UserDoc> users = userRepository.findUsersByActive(true);
        assertEquals(1, users.size());
    }

    @Test
    void findUsersByLoginFragment() {
        List<UserDoc> users = userRepository.findUsersByLoginFragment("log");
        assertEquals(2, users.size());
    }

    @Test
    void findUserByFirstName() {
        List<UserDoc> users = userRepository.findUserByFirstName("Jan");
        assertEquals(1, users.size());
        assertEquals("Jan", users.getFirst().getFirstName());
    }

    @Test
    void findUserByLastName() {
        List<UserDoc> users = userRepository.findUserByLastName("Nowak");
        assertEquals(1, users.size());
        assertEquals("Nowak", users.getFirst().getLastName());
    }
}