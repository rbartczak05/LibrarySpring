package pl.lodz.p.library.adapters.mongo.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;
import pl.lodz.p.library.adapters.mongo.mappers.LoanMapper;
import pl.lodz.p.library.adapters.mongo.repositories.LoanRepository;
import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryAdapterTest {

    @Mock
    private LoanRepository repository;

    @Spy
    private LoanMapper mapper = new LoanMapper();

    @InjectMocks
    private LoanRepositoryAdapter adapter;

    @Test
    void findById() {
        LoanDoc doc = new LoanDoc("r1", "b1", LocalDateTime.now());
        doc.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(doc));

        Optional<Loan> result = adapter.findById("1");
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    void findByClientId() {
        when(repository.findByClientId("r1")).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findByClientId("r1");
        assertEquals(1, result.size());
    }

    @Test
    void findByBookSetId() {
        when(repository.findByBookSetId("b1")).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findByBookSetId("b1");
        assertEquals(1, result.size());
    }

    @Test
    void findByClientIdAndBookSetId() {
        when(repository.findByClientIdAndBookSetId("r1", "b1")).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findByClientIdAndBookSetId("r1", "b1");
        assertEquals(1, result.size());
    }

    @Test
    void findByActive() {
        when(repository.findByActive(true)).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findByActive(true);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookSetIdAndActive() {
        when(repository.findByBookSetIdAndActive("b1", true)).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findByBookSetIdAndActive("b1", true);
        assertEquals(1, result.size());
    }

    @Test
    void findByClientIdAndActive() {
        when(repository.findByClientIdAndActive("r1", true)).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findByClientIdAndActive("r1", true);
        assertEquals(1, result.size());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(new LoanDoc("r1", "b1", LocalDateTime.now())));
        List<Loan> result = adapter.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void createLoan() {
        LoanDoc doc = new LoanDoc("r1", "b1", LocalDateTime.now());
        doc.setId("1");
        when(repository.save(any(LoanDoc.class))).thenReturn(doc);

        Optional<Loan> result = adapter.createLoan("r1", "b1");
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
    }

    @Test
    void createLoanWithTime() {
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        LoanDoc doc = new LoanDoc("r1", "b1", time);
        doc.setId("1");
        when(repository.save(any(LoanDoc.class))).thenReturn(doc);

        Optional<Loan> result = adapter.createLoan("r1", "b1", time);
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals(time, result.get().getStartTime());
    }

    @Test
    void updateLoan() {
        LoanDoc existing = new LoanDoc("r1", "b1", LocalDateTime.now());
        existing.setId("1");

        Loan updates = new Loan("r1", "b1", LocalDateTime.now().plusDays(1));
        updates.setEndTime(LocalDateTime.now().plusDays(10));

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(LoanDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Loan> result = adapter.updateLoan("1", updates);
        assertTrue(result.isPresent());
        assertEquals(updates.getStartTime(), result.get().getStartTime());
        assertEquals(updates.getEndTime(), result.get().getEndTime());
    }

    @Test
    void endLoan() {
        LoanDoc existing = new LoanDoc("r1", "b1", LocalDateTime.now());
        existing.setId("1");
        existing.setActive(true);

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(LoanDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Loan> result = adapter.endLoan("1");
        assertTrue(result.isPresent());
        assertFalse(result.get().isActive());
        assertNotNull(result.get().getReturnTime());
    }

    @Test
    void deleteLoan() {
        adapter.deleteLoan("1");
        verify(repository, times(1)).deleteById("1");
    }
}