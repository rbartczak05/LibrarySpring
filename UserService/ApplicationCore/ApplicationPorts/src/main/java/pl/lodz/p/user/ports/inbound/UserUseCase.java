package pl.lodz.p.user.ports.inbound;

import pl.lodz.p.user.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    User findUserById(UUID id);

    User findUserByLogin(String login);

    User findUserByEmail(String email);

    List<User> findUsersByAge(int age);

    List<User> findUsersByActive(boolean active);

    List<User> findUsersByLoginFragment(String loginFragment);

    List<User> findAllUsers();

    void changeUserPasswordInModel(User user, String newPasswordEncrypted);

    User addUser(User user);

    User updateUser(UUID id, User userUpdates);

    User activateUser(UUID id);

    User deactivateUser(UUID id);

    void deleteUser(UUID id);
}