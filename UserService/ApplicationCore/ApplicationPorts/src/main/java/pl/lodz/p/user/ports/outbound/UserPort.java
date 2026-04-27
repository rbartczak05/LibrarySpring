package pl.lodz.p.user.ports.outbound;

import pl.lodz.p.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPort {
    Optional<User> findUserById(String id);

    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByEmail(String email);

    List<User> findUserByFirstName(String firstName);

    List<User> findUserByLastName(String firstName);

    List<User> findUsersByAge(int age);

    List<User> findUsersByActive(boolean active);

    List<User> findUsersByLoginFragment(String loginFragment);

    List<User> findAllUsers();

    Optional<User> addUser(User user);

    Optional<User> updateUser(String id, User userUpdates);

    Optional<User> activateUser(String id);

    Optional<User> deactivateUser(String id);

    void deleteUser(String id);
}
