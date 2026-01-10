package pl.lodz.p.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.exception.*;
import pl.lodz.p.library.model.User;
import pl.lodz.p.library.repository.UserRepository;

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
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.CONFLICT, "User with id: " + id + " does not exists"));
    }

    public User findUserByLogin(String login) {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new UserLoginAlreadyExistException(HttpStatus.CONFLICT, "User with login: " + login + " already exists"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserEmailAlreadyExistException(HttpStatus.CONFLICT, "User with email: " + email + " already exists"));
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

    @Transactional
    public User addUser(User user) {
        if (user.getId() != null && userRepository.findById(user.getId()).isPresent()) {
            throw new IdException(HttpStatus.CONFLICT, "User with ID: " + user.getId() + " already exists");
        }
        if (userRepository.findUserByLogin(user.getLogin()).isPresent()) {
            throw new UserLoginAlreadyExistException(HttpStatus.CONFLICT, "User with login: " + user.getLogin() + " already exists");
        }
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserEmailAlreadyExistException(HttpStatus.CONFLICT, "User with email: " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(String id, User userUpdates) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " not found.");
        }
        User existingUser = findUserById(id);

        if (userUpdates == null) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User Updates not found");
        }
        userRepository.findUserByLogin(userUpdates.getLogin()).ifPresent(foundUser -> {
            if (!foundUser.getId().equals(id)) {
                throw new UserLoginAlreadyExistException(HttpStatus.CONFLICT, "Login: " + userUpdates.getLogin() + " is already taken by another user");
            }
        });
        userRepository.findUserByEmail(userUpdates.getEmail()).ifPresent(foundUser -> {
            if (!foundUser.getId().equals(id)) {
                throw new UserEmailAlreadyExistException(HttpStatus.CONFLICT, "Email: " + userUpdates.getEmail() + " is already taken by another user");
            }
        });


        existingUser.setLogin(userUpdates.getLogin());
        existingUser.setEmail(userUpdates.getEmail());
        existingUser.setAge(userUpdates.getAge());
        existingUser.setActive(userUpdates.isActive());

        return userRepository.save(existingUser);
    }

    @Transactional
    public User activateUser(String id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " cannot be activated. User not found.");
        }

        User user = findUserById(id);
        if (user.isActive()) {
            throw new UserStateException(HttpStatus.CONFLICT, "User with id: " + id + " is already active.");
        }

        user.setActive(true);
        return userRepository.save(user);
    }

    @Transactional
    public User deactivateUser(String id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND, "User with id: " + id + " cannot be deactivated. User not found.");
        }

        User user = findUserById(id);
        if (!user.isActive()) {
            throw new UserStateException(HttpStatus.CONFLICT, "User with id: " + id + " is already active.");
        }

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