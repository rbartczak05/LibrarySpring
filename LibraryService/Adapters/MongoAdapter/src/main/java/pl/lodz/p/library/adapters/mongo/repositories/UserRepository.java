package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDoc, String> {
    Optional<UserDoc> findUserByLogin(String login);

    Optional<UserDoc> findUserByEmail(String email);

    List<UserDoc> findUsersByAge(int age);

    List<UserDoc> findUsersByActive(boolean active);

    @Query("{ 'login': { $regex: ?0} }")
    List<UserDoc> findUsersByLoginFragment(String loginFragment);
}
