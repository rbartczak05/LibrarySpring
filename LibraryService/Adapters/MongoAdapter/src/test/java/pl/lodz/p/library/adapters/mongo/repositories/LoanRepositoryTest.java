package pl.lodz.p.library.adapters.mongo.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.adapters.mongo.documents.LoanDoc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
class LoanRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        LoanDoc loan1 = new LoanDoc("reader1", "book1", LocalDateTime.now());
        loan1.setActive(true);
        loanRepository.save(loan1);

        LoanDoc loan2 = new LoanDoc("reader2", "book2", LocalDateTime.now());
        loan2.setActive(false);
        loanRepository.save(loan2);
    }

    @AfterEach
    void tearDown() {
        loanRepository.deleteAll();
    }

    @Test
    void findByReaderId() {
        List<LoanDoc> loans = loanRepository.findByReaderId("reader1");
        assertEquals(1, loans.size());
    }

    @Test
    void findByBookSetId() {
        List<LoanDoc> loans = loanRepository.findByBookSetId("book2");
        assertEquals(1, loans.size());
    }

    @Test
    void findByReaderIdAndBookSetId() {
        List<LoanDoc> loans = loanRepository.findByReaderIdAndBookSetId("reader1", "book1");
        assertEquals(1, loans.size());
    }

    @Test
    void findByActive() {
        List<LoanDoc> activeLoans = loanRepository.findByActive(true);
        assertEquals(1, activeLoans.size());
    }

    @Test
    void findByBookSetIdAndActive() {
        List<LoanDoc> activeLoans = loanRepository.findByBookSetIdAndActive("book1", true);
        assertEquals(1, activeLoans.size());
    }

    @Test
    void findByReaderIdAndActive() {
        List<LoanDoc> loans = loanRepository.findByReaderIdAndActive("reader2", false);
        assertEquals(1, loans.size());
    }
}