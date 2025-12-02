package pl.lodz.p.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByAge(int age);

    List<User> findUsersByActive(boolean active);

    @Query("{ 'login': { $regex: ?0} }")
    List<User> findUsersByLoginFragment(String loginFragment);
}
