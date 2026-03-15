package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface GetUserPort {
    Optional<User> findUserById(String id);

    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByAge(int age);

    List<User> findUsersByActive(boolean active);

    List<User> findUsersByLoginFragment(String loginFragment);

    List<User> findAllUsers();
}
