package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.User;

import java.util.Optional;

public interface SaveUserPort {
    Optional<User> addUser(User user);

    Optional<User> updateUser(String id, User userUpdates);

    Optional<User> activateUser(String id);

    Optional<User> deactivateUser(String id);
}
