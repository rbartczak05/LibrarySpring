package pl.lodz.p.library.ports.infrastructure;

import pl.lodz.p.library.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findUserById(String id);
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);
    List<User> findUsersByAge(int age);
    List<User> findUsersByActive(boolean active);
    List<User> findUsersByLoginFragment(String loginFragment);
    List<User> findAllUsers();
    Optional<User> addUser(User user);
    Optional<User> updateUser(String id, User userUpdates);
    Optional<User> activateUser(String id);
    Optional<User> deactivateUser(String id);
}
