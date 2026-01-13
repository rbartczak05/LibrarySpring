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

class UserServiceTest extends BaseServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
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
    void activateUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);

        User savedUser = userRepository.save(reader);
        Assertions.assertFalse(savedUser.isActive());

        User activatedUser = userService.activateUser(savedUser.getId());

        Assertions.assertTrue(activatedUser.isActive());
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
}