package pl.lodz.p.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.exception.*;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "Użytkownik o ID: " + id + " nie istnieje"));
    }

    public User findUserByLogin(String login) {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "Użytkownik o loginie: " + login + " nie istnieje"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND, "Użytkownik o email-u: " + email + " nie istnieje"));
    }

    public List<User> findUsersByAge(int age) {
        return userRepository.findUsersByAge(age);
    }

    public List<User> findUsersByActive(boolean active) {
        return userRepository.findUsersByActive(active);
    }

    public List<User> findUsersByLoginFragment(String loginFragment) {
        return userRepository.findUsersByLoginFragment(loginFragment);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void changeUserPasswordInModel(User user, String newPasswordEncrypted) {
        try {
            Field passwordField = pl.lodz.p.library.model.User.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(user, newPasswordEncrypted);
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się ustawić hasła", e);
        }
    }

    @Transactional
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // bez zmiany loginu
    @Transactional
    public User updateUser(String id, User userUpdates) {
        User existingUser = findUserById(id);

//        existingUser.setLogin(userUpdates.getLogin());
        existingUser.setEmail(userUpdates.getEmail());
        existingUser.setAge(userUpdates.getAge());
        existingUser.setActive(userUpdates.isActive());

        return userRepository.save(existingUser);
    }

    @Transactional
    public User activateUser(String id) {
        User user = findUserById(id);

        user.setActive(true);

        return userRepository.save(user);
    }

    @Transactional
    public User deactivateUser(String id) {
        User user = findUserById(id);

        user.setActive(false);

        return userRepository.save(user);
    }

    /// Zachowane, żeby w razie czego mieć taką funkcję i udostępniać całego CRU(D)-a w aplikacji dla użytkownika.
//    @Transactional
//    public void deleteUser(String id) {
//        if (!userRepository.existsById(id)) {
//            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " does not exist");
//        }
//        userRepository.deleteById(id);
//    }
}