package pl.lodz.p.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.exception.*;
import pl.lodz.p.library.model.Administrator;
import pl.lodz.p.library.model.Librarian;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Testcontainers
@SpringBootTest
class UserServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
    @Autowired
    private UserRepository userRepository;
    private UserService userService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        userRepository.deleteAll();
    }

    @Test
    void addUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        reader.setId(UUID.randomUUID().toString());

        User savedUser = userService.addUser(reader);

        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("testUser", savedUser.getLogin());
        Assertions.assertEquals(1, userRepository.count());
    }

    @Test
    void findAllTest() {
        userRepository.save(new Reader("testReader", "testReader@gmail.com", 20));
        userRepository.save(new Librarian("testLib", "testLib@gmail.com", 30));
        userRepository.save(new Administrator("testAdmin", "testAdmin@gmail.com", 40));

        List<User> users = userService.findAllUsers();

        Assertions.assertEquals(3, users.size());
    }

    @Test
    void findUserByIdTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        User savedUser = userRepository.save(reader);

        User foundUser = userService.findUserById(savedUser.getId());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("testUser", foundUser.getLogin());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserById(UUID.randomUUID().toString()));
    }

    @Test
    void findUsersByLoginFragmentTest() {
        userRepository.save(new Reader("janeczek", "janeczek@gmail.com", 20));
        userRepository.save(new Reader("krzysiek", "krzysiek@gmail.com", 30));
        userRepository.save(new Reader("janeczka", "janeczka@gmail.com", 40));

        List<User> users = userService.findUsersByLoginFragment("jan");

        Assertions.assertEquals(2, users.size());
    }

    @Test
    void findUserByLoginTest() {
        userRepository.save(new Reader("testUser", "test@gmail.com", 25));

        Optional<User> found = Optional.ofNullable(userService.findUserByLogin("testUser"));

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("test@gmail.com", found.get().getEmail());
    }

    @Test
    void findUserByEmailTest() {
        userRepository.save(new Reader("testUser", "test@gmail.com", 25));

        Optional<User> found = Optional.ofNullable(userService.findUserByEmail("test@gmail.com"));

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("testUser", found.get().getLogin());
    }

    @Test
    void findUsersByAgeTest() {
        userRepository.save(new Reader("testUser", "test@gmail.com", 25));

        List<User> foundUsers = userService.findUsersByAge(25);

        Assertions.assertEquals(1, foundUsers.size());
        Assertions.assertEquals("testUser", foundUsers.getFirst().getLogin());
    }

    @Test
    void addUserWithExistingId() {
        Reader reader1 = new Reader("login1", "email1@example.com", 20);
        reader1.setId(UUID.randomUUID().toString());
        userRepository.save(reader1);
        Reader reader2 = new Reader("login2", "email2@example.com", 20);
        reader2.setId(reader1.getId());

        Assertions.assertThrows(IdException.class, () -> userService.addUser(reader2));

    }

    @Test
    void addUserExistingLoginTest() {
        userRepository.save(new Reader("istniejacyLogin", "test1@gmail.com", 30));
        Reader nowyReader = new Reader("istniejacyLogin", "test2@gmail.com", 25);
        nowyReader.setId(UUID.randomUUID().toString());

        Assertions.assertThrows(UserLoginAlreadyExistException.class, () -> userService.addUser(nowyReader));
    }

    @Test
    void addUserExistingEmailTest() {
        userRepository.save(new Reader("test1", "istniejacy@gmail.com", 30));
        Reader nowyReader = new Reader("test2", "istniejacy@gmail.com", 25);
        nowyReader.setId(UUID.randomUUID().toString());

        Assertions.assertThrows(UserEmailAlreadyExistException.class, () -> userService.addUser(nowyReader));
    }

    @Test
    void updateUserTest() {
        Reader reader = userRepository.save(new Reader("staryLogin", "stary@gmail.com", 20));
        String readerId = reader.getId();

        Reader updates = new Reader("nowyLogin", "nowy@gmail.com", 25);

        User updatedUser = userService.updateUser(readerId, updates);

        Assertions.assertEquals(readerId, updatedUser.getId());
        Assertions.assertEquals("nowyLogin", updatedUser.getLogin());
        Assertions.assertEquals("nowy@gmail.com", updatedUser.getEmail());
        Assertions.assertEquals(25, updatedUser.getAge());
    }

    @Test
    void updateUserFailTest() {
        userRepository.save(new Reader("zajetyLogin", "zajety@gmail.com", 30));
        Reader updateReader = userRepository.save(new Reader("staryLogin", "stary@gmail.com", 20));
        String readerId = updateReader.getId();

        Reader updates = new Reader("zajetyLogin", "nowy@gmail.com", 25);

        Assertions.assertThrows(UserLoginAlreadyExistException.class, () -> userService.updateUser(readerId, updates));
    }

    @Test
    void activateUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);

        User savedUser = userRepository.save(reader);
        Assertions.assertFalse(savedUser.isActive());

        User activatedUser = userService.activateUser(savedUser.getId());

        Assertions.assertTrue(activatedUser.isActive());
    }

    @Test
    void activateUserFailTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        reader.setActive(true);
        User savedUser = userRepository.save(reader);
        Assertions.assertTrue(savedUser.isActive());

        Assertions.assertThrows(UserStateException.class, () -> userService.activateUser(savedUser.getId()));
    }

    @Test
    void deactivateUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        reader.setActive(true);
        User savedUser = userRepository.save(reader);
        Assertions.assertTrue(savedUser.isActive());

        User deactivatedUser = userService.deactivateUser(savedUser.getId());

        Assertions.assertFalse(deactivatedUser.isActive());
    }

    @Test
    void deactivateUserFailTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        reader.setActive(false);
        User savedUser = userRepository.save(reader);
        Assertions.assertFalse(savedUser.isActive());

        Assertions.assertThrows(UserStateException.class, () -> userService.deactivateUser(savedUser.getId()));
    }
}