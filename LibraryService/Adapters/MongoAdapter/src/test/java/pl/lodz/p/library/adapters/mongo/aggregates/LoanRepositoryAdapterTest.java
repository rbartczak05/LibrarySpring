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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryAdapterTest {

    private final UUID clientId = UUID.randomUUID();
    private final UUID bookSetId = UUID.randomUUID();
    private final UUID loanId = UUID.randomUUID();
    @Mock
    private LoanRepository repository;
    @Spy
    private LoanMapper mapper = new LoanMapper();
    @InjectMocks
    private LoanRepositoryAdapter adapter;

    @Test
    void findById() {
        LoanDoc doc = new LoanDoc(clientId, bookSetId, LocalDateTime.now());
        doc.setId(loanId);
        when(repository.findById(loanId)).thenReturn(Optional.of(doc));

        Optional<Loan> result = adapter.findById(loanId);

        assertTrue(result.isPresent());
        assertEquals(loanId, result.get().getId());
    }

    @Test
    void findByClientId() {
        when(repository.findByClientId(clientId)).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findByClientId(clientId);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookSetId() {
        when(repository.findByBookSetId(bookSetId)).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findByBookSetId(bookSetId);
        assertEquals(1, result.size());
    }

    @Test
    void findByClientIdAndBookSetId() {
        when(repository.findByClientIdAndBookSetId(clientId, bookSetId)).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findByClientIdAndBookSetId(clientId, bookSetId);
        assertEquals(1, result.size());
    }

    @Test
    void findByActive() {
        when(repository.findByActive(true)).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findByActive(true);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookSetIdAndActive() {
        when(repository.findByBookSetIdAndActive(bookSetId, true)).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findByBookSetIdAndActive(bookSetId, true);
        assertEquals(1, result.size());
    }

    @Test
    void findByClientIdAndActive() {
        when(repository.findByClientIdAndActive(clientId, true)).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findByClientIdAndActive(clientId, true);
        assertEquals(1, result.size());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(new LoanDoc(clientId, bookSetId, LocalDateTime.now())));
        List<Loan> result = adapter.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void createLoan() {
        LoanDoc doc = new LoanDoc(clientId, bookSetId, LocalDateTime.now());
        doc.setId(loanId);
        when(repository.save(any(LoanDoc.class))).thenReturn(doc);

        Optional<Loan> result = adapter.createLoan(clientId, bookSetId);

        assertTrue(result.isPresent());
        assertEquals(loanId, result.get().getId());
    }

    @Test
    void createLoanWithTime() {
        LocalDateTime time = LocalDateTime.now().minusDays(1).withNano(0);
        LoanDoc doc = new LoanDoc(clientId, bookSetId, time);
        doc.setId(loanId);
        when(repository.save(any(LoanDoc.class))).thenReturn(doc);

        Optional<Loan> result = adapter.createLoan(clientId, bookSetId, time);

        assertTrue(result.isPresent());
        assertEquals(loanId, result.get().getId());
        assertEquals(time, result.get().getStartTime());
    }

    @Test
    void updateLoan() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        LoanDoc existing = new LoanDoc(clientId, bookSetId, now);
        existing.setId(loanId);

        LocalDateTime newStart = now.plusDays(1);
        Loan updates = new Loan(clientId, bookSetId, newStart);
        updates.setEndTime(now.plusDays(10));

        when(repository.findById(loanId)).thenReturn(Optional.of(existing));
        when(repository.save(any(LoanDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Loan> result = adapter.updateLoan(loanId, updates);

        assertTrue(result.isPresent());
        assertEquals(newStart, result.get().getStartTime());
        assertEquals(updates.getEndTime(), result.get().getEndTime());
    }

    @Test
    void endLoan() {
        LoanDoc existing = new LoanDoc(clientId, bookSetId, LocalDateTime.now());
        existing.setId(loanId);
        existing.setActive(true);

        when(repository.findById(loanId)).thenReturn(Optional.of(existing));
        when(repository.save(any(LoanDoc.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Loan> result = adapter.endLoan(loanId);

        assertTrue(result.isPresent());
        assertFalse(result.get().isActive());
        assertNotNull(result.get().getReturnTime());
    }

    @Test
    void deleteLoan() {
        adapter.deleteLoan(loanId);
        verify(repository, times(1)).deleteById(loanId);
    }
}