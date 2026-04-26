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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository repository;

    @Spy
    private AdministratorMapper adminMapper;

    @Spy
    private LibrarianMapper libMapper;

    @Spy
    private ReaderMapper readerMapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    void addUser() {
        Reader user = new Reader("r", "p", "e", "Jan", "Kowalski", 20);
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> {
            UserDoc doc = i.getArgument(0);
            doc.setId("1");
            return doc;
        });

        Optional<User> result = adapter.addUser(user);
        if (result.isPresent()) {
            assertNotNull(result.get().getId());
            assertEquals("r", result.get().getLogin());
        }
    }

    @Test
    void findUserById() {
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, false);
        doc.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserById("1");
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertInstanceOf(Reader.class, result.get());
    }

    @Test
    void findUserByLogin() {
        LibrarianDoc doc = new LibrarianDoc("l", "p", "e", "Anna", "Nowak", 30, false);
        doc.setId("2");
        when(repository.findUserByLogin("l")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserByLogin("l");
        assertTrue(result.isPresent());
        assertEquals("2", result.get().getId());
        assertInstanceOf(Librarian.class, result.get());
    }

    @Test
    void findUserByEmail() {
        AdministratorDoc doc = new AdministratorDoc("a", "p", "a@test.pl", "Adam", "Admin", 40, true);
        doc.setId("3");
        when(repository.findUserByEmail("a@test.pl")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserByEmail("a@test.pl");
        assertTrue(result.isPresent());
        assertEquals("3", result.get().getId());
        assertInstanceOf(Administrator.class, result.get());
    }

    @Test
    void findUsersByLoginFragment() {
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Piotr", "Wiśniewski", 25, false);
        doc.setId("4");
        when(repository.findUsersByLoginFragment("frag")).thenReturn(List.of(doc));

        List<User> result = adapter.findUsersByLoginFragment("frag");
        assertEquals(1, result.size());
        assertEquals("4", result.getFirst().getId());
    }

    @Test
    void findUserByFirstName() {
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 25, false);
        doc.setId("5");
        when(repository.findUserByFirstName("Jan")).thenReturn(List.of(doc));

        List<User> result = adapter.findUserByFirstName("Jan");
        assertEquals(1, result.size());
        assertEquals("Jan", result.getFirst().getFirstName());
    }

    @Test
    void findUserByLastName() {
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 25, false);
        doc.setId("6");
        when(repository.findUserByLastName("Kowalski")).thenReturn(List.of(doc));

        List<User> result = adapter.findUserByLastName("Kowalski");
        assertEquals(1, result.size());
        assertEquals("Kowalski", result.getFirst().getLastName());
    }

    @Test
    void getAllUsers() {
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Tomasz", "Zieliński", 22, true);
        doc.setId("5");
        when(repository.findAll()).thenReturn(List.of(doc));

        List<User> result = adapter.findAllUsers();
        assertEquals(1, result.size());
        assertEquals("5", result.getFirst().getId());
    }

    @Test
    void updateUser() {
        ReaderDoc existing = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, false);
        existing.setId("1");
        Reader updates = new Reader("newR", "newP", "newE", "Nowy", "User", 25);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.updateUser("1", updates);
        assertTrue(result.isPresent());
        assertEquals("newR", result.get().getLogin());
        assertEquals("newE", result.get().getEmail());
        assertEquals("Nowy", result.get().getFirstName());
        assertEquals("User", result.get().getLastName());
        assertEquals(25, result.get().getAge());
        assertFalse(result.get().isActive());
    }

    @Test
    void activateUser() {
        ReaderDoc existing = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, false);
        existing.setId("1");

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.activateUser("1");
        assertTrue(result.isPresent());
        assertTrue(result.get().isActive());
    }

    @Test
    void deactivateUser() {
        ReaderDoc existing = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, true);
        existing.setId("1");

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.deactivateUser("1");
        assertTrue(result.isPresent());
        assertFalse(result.get().isActive());
    }
}