package pl.lodz.p.library.adapters.mongo.documents;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LoanDocTest {

    @Test
    void testLoanDoc() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(7);
        LocalDateTime returned = start.plusDays(5);

        LoanDoc doc = new LoanDoc("readerId", "bookSetId", start);
        doc.setId("123");
        doc.setEndTime(end);
        doc.setReturnTime(returned);
        doc.setActive(false);

        assertEquals("123", doc.getId());
        assertEquals("readerId", doc.getReaderId());
        assertEquals("bookSetId", doc.getBookSetId());
        assertEquals(start, doc.getStartTime());
        assertEquals(end, doc.getEndTime());
        assertEquals(returned, doc.getReturnTime());
        assertFalse(doc.isActive());
    }
}