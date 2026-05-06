package pl.lodz.p.library.adapters.mongo.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.library.adapters.mongo.documents.ClientDoc;
import pl.lodz.p.library.adapters.mongo.mappers.ClientMapper;
import pl.lodz.p.library.adapters.mongo.repositories.ClientRepository;
import pl.lodz.p.library.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryAdapterTest {

    @Mock
    private ClientRepository repository;

    @Spy
    private ClientMapper mapper = new ClientMapper();

    @InjectMocks
    private ClientRepositoryAdapter adapter;

    @Test
    void findById() {
        UUID id = UUID.randomUUID();
        ClientDoc doc = new ClientDoc("Jan", "Kowalski", "jan@test.pl", 20);
        doc.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(doc));

        Optional<Client> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Jan", result.get().getFirstName());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(
                new ClientDoc("Jan", "Kowalski", "jan@test.pl", 20),
                new ClientDoc("Anna", "Nowak", "anna@test.pl", 30)
        ));

        List<Client> result = adapter.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        Client client = new Client("Jan", "Kowalski", "jan@test.pl", 20);
        ClientDoc doc = new ClientDoc("Jan", "Kowalski", "jan@test.pl", 20);
        doc.setId(id);

        when(repository.save(any(ClientDoc.class))).thenReturn(doc);

        Client result = adapter.save(client);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void deleteById() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(repository, times(1)).deleteById(id);
    }
}