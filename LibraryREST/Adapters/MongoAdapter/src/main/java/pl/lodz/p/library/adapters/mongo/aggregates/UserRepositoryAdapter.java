package pl.lodz.p.library.adapters.mongo.aggregates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.library.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.library.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.library.adapters.mongo.documents.UserDoc;
import pl.lodz.p.library.adapters.mongo.mappers.AdministratorMapper;
import pl.lodz.p.library.adapters.mongo.mappers.LibrarianMapper;
import pl.lodz.p.library.adapters.mongo.mappers.ReaderMapper;
import pl.lodz.p.library.adapters.mongo.repositories.UserRepository;
import pl.lodz.p.library.domain.model.Administrator;
import pl.lodz.p.library.domain.model.Librarian;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.domain.model.User;
import pl.lodz.p.library.ports.outbound.DeleteUserPort;
import pl.lodz.p.library.ports.outbound.GetUserPort;
import pl.lodz.p.library.ports.outbound.SaveUserPort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements GetUserPort, SaveUserPort, DeleteUserPort {

    private final UserRepository repository;
    private final AdministratorMapper adminMapper;
    private final LibrarianMapper librarianMapper;
    private final ReaderMapper readerMapper;

    @Autowired
    public UserRepositoryAdapter(UserRepository repository,
                                 AdministratorMapper adminMapper,
                                 LibrarianMapper librarianMapper,
                                 ReaderMapper readerMapper) {
        this.repository = repository;
        this.adminMapper = adminMapper;
        this.librarianMapper = librarianMapper;
        this.readerMapper = readerMapper;
    }

    private User toDomain(UserDoc doc) {
        if (doc instanceof AdministratorDoc) return adminMapper.toDomain((AdministratorDoc) doc);
        if (doc instanceof LibrarianDoc) return librarianMapper.toDomain((LibrarianDoc) doc);
        if (doc instanceof ReaderDoc) return readerMapper.toDomain((ReaderDoc) doc);
        return null;
    }

    private UserDoc toDocument(User user) {
        if (user instanceof Administrator) return adminMapper.toDocument((Administrator) user);
        if (user instanceof Librarian) return librarianMapper.toDocument((Librarian) user);
        if (user instanceof Reader) return readerMapper.toDocument((Reader) user);
        return null;
    }

    @Override
    public Optional<User> findUserById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        return repository.findUserByLogin(login).map(this::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return repository.findUserByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findUsersByAge(int age) {
        return repository.findUsersByAge(age).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersByActive(boolean active) {
        return repository.findUsersByActive(active).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> findUsersByLoginFragment(String loginFragment) {
        return repository.findUsersByLoginFragment(loginFragment).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUsers() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<User> addUser(User user) {
        UserDoc saved = repository.save(toDocument(user));
        return Optional.ofNullable(toDomain(saved));
    }

    @Override
    public Optional<User> updateUser(String id, User userUpdates) {
        return repository.findById(id).map(existing -> {
            existing.setEmail(userUpdates.getEmail());
            existing.setAge(userUpdates.getAge());
            existing.setActive(userUpdates.isActive());
            return toDomain(repository.save(existing));
        });
    }

    @Override
    public Optional<User> activateUser(String id) {
        return repository.findById(id).map(existing -> {
            existing.setActive(true);
            return toDomain(repository.save(existing));
        });
    }

    @Override
    public Optional<User> deactivateUser(String id) {
        return repository.findById(id).map(existing -> {
            existing.setActive(false);
            return toDomain(repository.save(existing));
        });
    }

    @Override
    public void deleteUser(String id) {
        repository.deleteById(id);
    }
}