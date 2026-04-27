package pl.lodz.p.user.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.user.domain.exceptions.UserException;
import pl.lodz.p.user.domain.model.Reader;
import pl.lodz.p.user.domain.model.User;

import java.util.UUID;

class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void addUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", "Test", "User", 25);
        User savedUser = userService.addUser(reader);

        Assertions.assertNotNull(savedUser.getId());
        Assertions.assertEquals("testUser", savedUser.getLogin());
        Assertions.assertEquals(1, userService.findAllUsers().size());
    }

    @Test
    void findUserByIdTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", "Test", "User", 25);
        User savedUser = userPort.addUser(reader).orElseThrow();

        User foundUser = userService.findUserById(savedUser.getId());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("testUser", foundUser.getLogin());
    }

    @Test
    void findUserByIdFailNotFoundTest() {
        Assertions.assertThrows(UserException.class, () -> userService.findUserById(UUID.randomUUID().toString()));
    }

    @Test
    void activateUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", "Test", "User", 25);
        User savedUser = userPort.addUser(reader).orElseThrow();

        User activatedUser = userService.activateUser(savedUser.getId());

        Assertions.assertTrue(activatedUser.isActive());
    }

    @Test
    void deactivateUserTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", "Test", "User", 25);
        reader.setActive(true);
        User savedUser = userPort.addUser(reader).orElseThrow();

        User deactivatedUser = userService.deactivateUser(savedUser.getId());

        Assertions.assertFalse(deactivatedUser.isActive());
    }
}