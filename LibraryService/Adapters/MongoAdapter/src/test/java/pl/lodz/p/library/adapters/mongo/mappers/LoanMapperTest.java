package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;
import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoanMapperTest {

    private final LoanMapper mapper = new LoanMapper();

    @Test
    void toDomain() {
        UUID id = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID bookSetId = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 2, 1, 10, 0);
        LocalDateTime returned = LocalDateTime.of(2023, 1, 15, 10, 0);

        LoanDoc doc = new LoanDoc(clientId, bookSetId, start);
        doc.setId(id);
        doc.setEndTime(end);
        doc.setReturnTime(returned);
        doc.setActive(false);

        Loan loan = mapper.toDomain(doc);

        assertNotNull(loan);
        assertEquals(id, loan.getId());
        assertEquals(clientId, loan.getClientId());
        assertEquals(bookSetId, loan.getBookSetId());
        assertEquals(start, loan.getStartTime());
        assertEquals(end, loan.getEndTime());
        assertEquals(returned, loan.getReturnTime());
        assertFalse(loan.isActive());
    }

    @Test
    void toDomainNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDocument() {
        UUID id = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID bookSetId = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 2, 1, 10, 0);
        LocalDateTime returned = LocalDateTime.of(2023, 1, 15, 10, 0);

        Loan loan = new Loan(clientId, bookSetId, start);
        loan.setId(id);
        loan.setEndTime(end);
        loan.setReturnTime(returned);
        loan.setActive(false);

        LoanDoc doc = mapper.toDocument(loan);

        assertNotNull(doc);
        assertEquals(id, doc.getId());
        assertEquals(clientId, doc.getClientId());
        assertEquals(bookSetId, doc.getBookSetId());
        assertEquals(start, doc.getStartTime());
        assertEquals(end, doc.getEndTime());
        assertEquals(returned, doc.getReturnTime());
        assertFalse(doc.isActive());
    }

    @Test
    void toDocumentNull() {
        assertNull(mapper.toDocument(null));
    }
}