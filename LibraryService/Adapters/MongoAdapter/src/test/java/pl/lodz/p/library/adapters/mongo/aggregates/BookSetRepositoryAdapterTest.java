package pl.lodz.p.library.adapters.mongo.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.adapters.mongo.mappers.BookSetMapper;
import pl.lodz.p.library.adapters.mongo.repositories.BookSetRepository;
import pl.lodz.p.library.domain.model.BookSet;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookSetRepositoryAdapterTest {

    @Mock
    private BookSetRepository repository;

    @Spy
    private BookSetMapper mapper = new BookSetMapper();

    @InjectMocks
    private BookSetRepositoryAdapter adapter;

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findById() {
        BookSetDoc doc = new BookSetDoc("T", "A", 2000, 1);
        doc.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(doc));
        Optional<BookSet> result = adapter.findById("1");
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    void findBookSetsByTitle() {
        when(repository.findBookByTitle("T")).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findBookSetsByTitle("T");
        assertEquals(1, result.size());
    }

    @Test
    void findBookSetsByAuthor() {
        when(repository.findBooksByAuthor("A")).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findBookSetsByAuthor("A");
        assertEquals(1, result.size());
    }

    @Test
    void findBookSetsByReleaseYear() {
        when(repository.findBooksByReleaseYear(2000)).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findBookSetsByReleaseYear(2000);
        assertEquals(1, result.size());
    }

    @Test
    void findBookSetsByQuantity() {
        when(repository.findBooksByQuantity(1)).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findBookSetsByQuantity(1);
        assertEquals(1, result.size());
    }

    @Test
    void findAllBookSetsByQuantity() {
        when(repository.findBooksByQuantity(1)).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findAllBookSetsByQuantity(1);
        assertEquals(1, result.size());
    }

    @Test
    void findByQuantityGreaterThan() {
        when(repository.findByQuantityGreaterThan(0)).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findByQuantityGreaterThan(0);
        assertEquals(1, result.size());
    }

    @Test
    void findByQuantityLessThan() {
        BookSetDoc doc1 = new BookSetDoc("T1", "A", 2000, 1);
        when(repository.findByQuantityLessThan(3)).thenReturn(List.of(doc1));

        List<BookSet> result = adapter.findByQuantityLessThan(3);

        assertEquals(1, result.size());
        assertEquals("T1", result.getFirst().getTitle());
    }

    @Test
    void findBookSetsByAvailableTrue() {
        when(repository.findByQuantityGreaterThan(0)).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findBookSetsByAvailable(true);
        assertEquals(1, result.size());
    }

    @Test
    void findBookSetsByAvailableFalse() {
        when(repository.findBooksByQuantity(0)).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 0)));
        List<BookSet> result = adapter.findBookSetsByAvailable(false);
        assertEquals(1, result.size());
    }

    @Test
    void findAllBookSets() {
        when(repository.findAll()).thenReturn(List.of(new BookSetDoc("T", "A", 2000, 1)));
        List<BookSet> result = adapter.findAllBookSets();
        assertEquals(1, result.size());
    }

    @Test
    void addBookSet() {
        BookSet domain = new BookSet("T", "A", 2000, 1);
        BookSetDoc doc = new BookSetDoc("T", "A", 2000, 1);
        doc.setId("1");

        when(repository.save(any(BookSetDoc.class))).thenReturn(doc);

        Optional<BookSet> result = adapter.addBookSet(domain);
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    void updateBookSet() {
        BookSetDoc existing = new BookSetDoc("T", "A", 2000, 1);
        existing.setId("1");
        BookSet updates = new BookSet("T", "A", 2000, 5);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(BookSetDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<BookSet> result = adapter.updateBookSet("1", updates);

        assertTrue(result.isPresent());
        assertEquals(5, result.get().getQuantity());
    }

    @Test
    void save() {
        BookSet domain = new BookSet("T", "A", 2000, 1);
        BookSetDoc doc = new BookSetDoc("T", "A", 2000, 1);
        doc.setId("1");

        when(repository.save(any(BookSetDoc.class))).thenReturn(doc);

        BookSet result = adapter.save(domain);
        assertEquals("1", result.getId());
    }

    @Test
    void deleteBookSet() {
        adapter.deleteBookSet("1");
        verify(repository, times(1)).deleteById("1");
    }
}