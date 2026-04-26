package pl.lodz.p.library.adapters.mongo.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository repository;

    @Spy
    private AdministratorMapper adminMapper = new AdministratorMapper();
    @Spy
    private LibrarianMapper librarianMapper = new LibrarianMapper();
    @Spy
    private ReaderMapper readerMapper = new ReaderMapper();

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    void findUserById() {
        ReaderDoc doc = new ReaderDoc("login", "pass", "email", 20);
        doc.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserById("1");
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof Reader);
        assertEquals("1", result.get().getId());
    }

    @Test
    void findUserByLogin() {
        AdministratorDoc doc = new AdministratorDoc("admin", "pass", "email", 30);
        when(repository.findUserByLogin("admin")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserByLogin("admin");
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof Administrator);
    }

    @Test
    void findUserByEmail() {
        LibrarianDoc doc = new LibrarianDoc("lib", "pass", "email", 40);
        when(repository.findUserByEmail("email")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserByEmail("email");
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof Librarian);
    }

    @Test
    void findUsersByAge() {
        when(repository.findUsersByAge(20)).thenReturn(List.of(new ReaderDoc("login", "pass", "email", 20)));
        List<User> result = adapter.findUsersByAge(20);
        assertEquals(1, result.size());
    }

    @Test
    void findUsersByActive() {
        when(repository.findUsersByActive(true)).thenReturn(List.of(new ReaderDoc("login", "pass", "email", 20)));
        List<User> result = adapter.findUsersByActive(true);
        assertEquals(1, result.size());
    }

    @Test
    void findUsersByLoginFragment() {
        when(repository.findUsersByLoginFragment("log")).thenReturn(List.of(new ReaderDoc("login", "pass", "email", 20)));
        List<User> result = adapter.findUsersByLoginFragment("log");
        assertEquals(1, result.size());
    }

    @Test
    void findAllUsers() {
        when(repository.findAll()).thenReturn(List.of(
                new ReaderDoc("r", "p", "e", 20),
                new AdministratorDoc("a", "p", "e", 30)
        ));
        List<User> result = adapter.findAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void addUserReader() {
        Reader reader = new Reader("r", "p", "e", 20);
        ReaderDoc doc = new ReaderDoc("r", "p", "e", 20);
        doc.setId("1");

        when(repository.save(any(UserDoc.class))).thenReturn(doc);

        Optional<User> result = adapter.addUser(reader);
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    void updateUser() {
        ReaderDoc existing = new ReaderDoc("r", "p", "e", 20);
        existing.setId("1");

        Reader updates = new Reader("newR", "p", "newE", 25);
        updates.setActive(false);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.updateUser("1", updates);
        assertTrue(result.isPresent());
        assertEquals("newR", result.get().getLogin());
        assertEquals("newE", result.get().getEmail());
        assertEquals(25, result.get().getAge());
        assertFalse(result.get().isActive());
    }

    @Test
    void activateUser() {
        ReaderDoc existing = new ReaderDoc("r", "p", "e", 20);
        existing.setId("1");
        existing.setActive(false);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.activateUser("1");
        assertTrue(result.isPresent());
        assertTrue(result.get().isActive());
    }

    @Test
    void deactivateUser() {
        ReaderDoc existing = new ReaderDoc("r", "p", "e", 20);
        existing.setId("1");
        existing.setActive(true);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.deactivateUser("1");
        assertTrue(result.isPresent());
        assertFalse(result.get().isActive());
    }

    @Test
    void deleteUser() {
        adapter.deleteUser("1");
        verify(repository, times(1)).deleteById("1");
    }
}