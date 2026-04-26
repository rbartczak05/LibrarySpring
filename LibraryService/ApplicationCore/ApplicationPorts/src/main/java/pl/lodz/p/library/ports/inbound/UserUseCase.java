package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.User;

import java.util.List;

public interface UserUseCase {
    User findUserById(String id);

    User findUserByLogin(String login);

    User findUserByEmail(String email);

    List<User> findUsersByAge(int age);

    List<User> findUsersByActive(boolean active);

    List<User> findUsersByLoginFragment(String loginFragment);

    List<User> findAllUsers();

    void changeUserPasswordInModel(User user, String newPasswordEncrypted);

    User addUser(User user);

    User updateUser(String id, User userUpdates);

    User activateUser(String id);

    User deactivateUser(String id);

    void deleteUser(String id);
}