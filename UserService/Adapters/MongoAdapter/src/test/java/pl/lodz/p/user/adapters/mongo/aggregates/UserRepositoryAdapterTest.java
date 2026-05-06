package pl.lodz.p.user.adapters.mongo.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.user.adapters.mongo.documents.AdministratorDoc;
import pl.lodz.p.user.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.user.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.user.adapters.mongo.documents.UserDoc;
import pl.lodz.p.user.adapters.mongo.mappers.AdministratorMapper;
import pl.lodz.p.user.adapters.mongo.mappers.LibrarianMapper;
import pl.lodz.p.user.adapters.mongo.mappers.ReaderMapper;
import pl.lodz.p.user.adapters.mongo.repositories.UserRepository;
import pl.lodz.p.user.domain.model.Administrator;
import pl.lodz.p.user.domain.model.Librarian;
import pl.lodz.p.user.domain.model.Reader;
import pl.lodz.p.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void addUser() {
        Reader user = new Reader("r", "p", "e", "Jan", "Kowalski", 20);
        UUID id = UUID.randomUUID();
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> {
            UserDoc doc = i.getArgument(0);
            doc.setId(id);
            return doc;
        });

        Optional<User> result = adapter.addUser(user);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals(id, result.get().getId());
        assertEquals("r", result.get().getLogin());
    }

    @Test
    void findUserById() {
        UUID id = UUID.randomUUID();
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, false);
        doc.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertInstanceOf(Reader.class, result.get());
    }

    @Test
    void findUserByLogin() {
        UUID id = UUID.randomUUID();
        LibrarianDoc doc = new LibrarianDoc("l", "p", "e", "Anna", "Nowak", 30, false);
        doc.setId(id);
        when(repository.findUserByLogin("l")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserByLogin("l");
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertInstanceOf(Librarian.class, result.get());
    }

    @Test
    void findUserByEmail() {
        UUID id = UUID.randomUUID();
        AdministratorDoc doc = new AdministratorDoc("a", "p", "a@test.pl", "Adam", "Admin", 40, true);
        doc.setId(id);
        when(repository.findUserByEmail("a@test.pl")).thenReturn(Optional.of(doc));

        Optional<User> result = adapter.findUserByEmail("a@test.pl");
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertInstanceOf(Administrator.class, result.get());
    }

    @Test
    void findUsersByLoginFragment() {
        UUID id = UUID.randomUUID();
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Piotr", "Wiśniewski", 25, false);
        doc.setId(id);
        when(repository.findUsersByLoginFragment("frag")).thenReturn(List.of(doc));

        List<User> result = adapter.findUsersByLoginFragment("frag");
        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().getId());
    }

    @Test
    void findUserByFirstName() {
        UUID id = UUID.randomUUID();
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 25, false);
        doc.setId(id);
        when(repository.findUserByFirstName("Jan")).thenReturn(List.of(doc));

        List<User> result = adapter.findUserByFirstName("Jan");
        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().getId());
        assertEquals("Jan", result.getFirst().getFirstName());
    }

    @Test
    void findUserByLastName() {
        UUID id = UUID.randomUUID();
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 25, false);
        doc.setId(id);
        when(repository.findUserByLastName("Kowalski")).thenReturn(List.of(doc));

        List<User> result = adapter.findUserByLastName("Kowalski");
        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().getId());
        assertEquals("Kowalski", result.getFirst().getLastName());
    }

    @Test
    void getAllUsers() {
        UUID id = UUID.randomUUID();
        ReaderDoc doc = new ReaderDoc("r", "p", "e", "Tomasz", "Zieliński", 22, true);
        doc.setId(id);
        when(repository.findAll()).thenReturn(List.of(doc));

        List<User> result = adapter.findAllUsers();
        assertEquals(1, result.size());
        assertEquals(id, result.getFirst().getId());
    }

    @Test
    void updateUser() {
        UUID id = UUID.randomUUID();
        ReaderDoc existing = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, false);
        existing.setId(id);
        Reader updates = new Reader("newR", "newP", "newE", "Nowy", "User", 25);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.updateUser(id, updates);
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
        UUID id = UUID.randomUUID();
        ReaderDoc existing = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, false);
        existing.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.activateUser(id);
        assertTrue(result.isPresent());
        assertTrue(result.get().isActive());
    }

    @Test
    void deactivateUser() {
        UUID id = UUID.randomUUID();
        ReaderDoc existing = new ReaderDoc("r", "p", "e", "Jan", "Kowalski", 20, true);
        existing.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(UserDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<User> result = adapter.deactivateUser(id);
        assertTrue(result.isPresent());
        assertFalse(result.get().isActive());
    }
}