package pl.lodz.p.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.domain.exceptions.UserException;
import pl.lodz.p.library.domain.model.User;
import pl.lodz.p.library.ports.inbound.UserUseCase;
import pl.lodz.p.library.ports.outbound.DeleteUserPort;
import pl.lodz.p.library.ports.outbound.GetUserPort;
import pl.lodz.p.library.ports.outbound.SaveUserPort;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class UserService implements UserUseCase {
    private final GetUserPort getUserPort;
    private final SaveUserPort saveUserPort;
    private final DeleteUserPort deleteUserPort;

    @Autowired
    public UserService(GetUserPort getUserPort, SaveUserPort saveUserPort, DeleteUserPort deleteUserPort) {
        this.getUserPort = getUserPort;
        this.saveUserPort = saveUserPort;
        this.deleteUserPort = deleteUserPort;
    }

    public User findUserById(String id) {
        return getUserPort.findUserById(id)
                .orElseThrow(() -> new UserException("Użytkownik o ID: " + id + " nie istnieje"));
    }

    public User findUserByLogin(String login) {
        return getUserPort.findUserByLogin(login)
                .orElseThrow(() -> new UserException("Użytkownik o loginie: " + login + " nie istnieje"));
    }

    public User findUserByEmail(String email) {
        return getUserPort.findUserByEmail(email)
                .orElseThrow(() -> new UserException("Użytkownik o email-u: " + email + " nie istnieje"));
    }

    public List<User> findUsersByAge(int age) {
        return getUserPort.findUsersByAge(age);
    }

    public List<User> findUsersByActive(boolean active) {
        return getUserPort.findUsersByActive(active);
    }

    public List<User> findUsersByLoginFragment(String loginFragment) {
        return getUserPort.findUsersByLoginFragment(loginFragment);
    }

    public List<User> findAllUsers() {
        return getUserPort.findAllUsers();
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
        return saveUserPort.addUser(user)
                .orElseThrow(() -> new UserException("Nie udało się dodać użytkownika"));
    }

    @Transactional
    public User updateUser(String id, User userUpdates) {
        return saveUserPort.updateUser(id, userUpdates)
                .orElseThrow(() -> new UserException("Nie udało się zaktualizować użytkownika"));
    }

    @Transactional
    public User activateUser(String id) {
        return saveUserPort.activateUser(id)
                .orElseThrow(() -> new UserException("Nie udało się aktywować użytkownika"));
    }

    @Transactional
    public User deactivateUser(String id) {
        return saveUserPort.deactivateUser(id)
                .orElseThrow(() -> new UserException("Nie udało się deaktywować użytkownika"));
    }

    @Transactional
    public void deleteUser(String id) {
        deleteUserPort.deleteUser(id);
    }
}