package pl.lodz.p.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.user.domain.exceptions.UserException;
import pl.lodz.p.user.domain.model.User;
import pl.lodz.p.user.ports.inbound.UserUseCase;
import pl.lodz.p.user.ports.outbound.UserPort;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserUseCase {
    private final UserPort userPort;

    @Autowired
    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    public User findUserById(UUID id) {
        return userPort.findUserById(id).orElseThrow(() -> new UserException("Użytkownik o ID: " + id + " nie istnieje"));
    }

    public User findUserByLogin(String login) {
        return userPort.findUserByLogin(login).orElseThrow(() -> new UserException("Użytkownik o loginie: " + login + " nie istnieje"));
    }

    public User findUserByEmail(String email) {
        return userPort.findUserByEmail(email).orElseThrow(() -> new UserException("Użytkownik o email-u: " + email + " nie istnieje"));
    }

    public List<User> findUsersByAge(int age) {
        return userPort.findUsersByAge(age);
    }

    public List<User> findUsersByActive(boolean active) {
        return userPort.findUsersByActive(active);
    }

    public List<User> findUsersByLoginFragment(String loginFragment) {
        return userPort.findUsersByLoginFragment(loginFragment);
    }

    public List<User> findAllUsers() {
        return userPort.findAllUsers();
    }

    public void changeUserPasswordInModel(User user, String newPasswordEncrypted) {
        try {
            Field passwordField = User.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(user, newPasswordEncrypted);
        } catch (Exception e) {
            throw new UserException("Nie udało się ustawić hasła");
        }
    }

    @Transactional
    public User addUser(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        return userPort.addUser(user).orElseThrow(() -> new UserException("Nie udało się dodać użytkownika"));
    }

    @Transactional
    public User updateUser(UUID id, User userUpdates) {
        return userPort.updateUser(id, userUpdates).orElseThrow(() -> new UserException("Nie udało się zaktualizować użytkownika"));
    }

    @Transactional
    public User activateUser(UUID id) {
        return userPort.activateUser(id).orElseThrow(() -> new UserException("Nie udało się aktywować użytkownika"));
    }

    @Transactional
    public User deactivateUser(UUID id) {
        return userPort.deactivateUser(id).orElseThrow(() -> new UserException("Nie udało się deaktywować użytkownika"));
    }

    @Transactional
    public void deleteUser(UUID id) {
        userPort.deleteUser(id);
    }
}