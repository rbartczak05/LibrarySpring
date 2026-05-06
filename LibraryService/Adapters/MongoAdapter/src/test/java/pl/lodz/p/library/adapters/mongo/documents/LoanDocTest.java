package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LoanDocTest {

    @Test
    void testLoanDoc() {
        UUID id = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID bookSetId = UUID.randomUUID();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        LocalDateTime returned = start.plusDays(5);

        LoanDoc doc = new LoanDoc(clientId, bookSetId, start);
        doc.setId(id);
        doc.setEndTime(end);
        doc.setReturnTime(returned);
        doc.setActive(false);

        assertEquals(id, doc.getId());
        assertEquals(clientId, doc.getClientId());
        assertEquals(bookSetId, doc.getBookSetId());
        assertEquals(start, doc.getStartTime());
        assertEquals(end, doc.getEndTime());
        assertEquals(returned, doc.getReturnTime());
        assertFalse(doc.isActive());
    }
}