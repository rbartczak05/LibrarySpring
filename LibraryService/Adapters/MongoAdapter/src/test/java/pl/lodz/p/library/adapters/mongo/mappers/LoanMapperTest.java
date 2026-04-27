package pl.lodz.p.library.adapters.mongo.mappers;

import org.junit.jupiter.api.Test;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;
import pl.lodz.p.library.domain.model.Loan;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoanMapperTest {

    private final LoanMapper mapper = new LoanMapper();

    @Test
    void toDomain() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 2, 1, 10, 0);
        LocalDateTime returned = LocalDateTime.of(2023, 1, 15, 10, 0);

        LoanDoc doc = new LoanDoc("client1", "book1", start);
        doc.setId("1");
        doc.setEndTime(end);
        doc.setReturnTime(returned);
        doc.setActive(false);

        Loan loan = mapper.toDomain(doc);

        assertNotNull(loan);
        assertEquals("1", loan.getId());
        assertEquals("client1", loan.getClientId());
        assertEquals("book1", loan.getBookSetId());
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
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 2, 1, 10, 0);
        LocalDateTime returned = LocalDateTime.of(2023, 1, 15, 10, 0);

        Loan loan = new Loan("client1", "book1", start);
        loan.setId("1");
        loan.setEndTime(end);
        loan.setReturnTime(returned);
        loan.setActive(false);

        LoanDoc doc = mapper.toDocument(loan);

        assertNotNull(doc);
        assertEquals("1", doc.getId());
        assertEquals("client1", doc.getClientId());
        assertEquals("book1", doc.getBookSetId());
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