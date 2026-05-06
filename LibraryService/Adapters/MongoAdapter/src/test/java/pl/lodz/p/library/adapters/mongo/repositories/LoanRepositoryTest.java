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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Testcontainers
class LoanRepositoryTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.2.7");

    @Autowired
    private LoanRepository loanRepository;

    private UUID client1Id;
    private UUID client2Id;
    private UUID book1Id;
    private UUID book2Id;

    @BeforeEach
    void setUp() {
        client1Id = UUID.randomUUID();
        client2Id = UUID.randomUUID();
        book1Id = UUID.randomUUID();
        book2Id = UUID.randomUUID();

        LoanDoc loan1 = new LoanDoc(client1Id, book1Id, LocalDateTime.now());
        loan1.setId(UUID.randomUUID());
        loan1.setActive(true);
        loanRepository.save(loan1);

        LoanDoc loan2 = new LoanDoc(client2Id, book2Id, LocalDateTime.now());
        loan2.setId(UUID.randomUUID());
        loan2.setActive(false);
        loanRepository.save(loan2);
    }

    @AfterEach
    void tearDown() {
        loanRepository.deleteAll();
    }

    @Test
    void findByClientId() {
        List<LoanDoc> loans = loanRepository.findByClientId(client1Id);
        assertEquals(1, loans.size());
    }

    @Test
    void findByBookSetId() {
        List<LoanDoc> loans = loanRepository.findByBookSetId(book2Id);
        assertEquals(1, loans.size());
    }

    @Test
    void findByClientIdAndBookSetId() {
        List<LoanDoc> loans = loanRepository.findByClientIdAndBookSetId(client1Id, book1Id);
        assertEquals(1, loans.size());
    }

    @Test
    void findByActive() {
        List<LoanDoc> activeLoans = loanRepository.findByActive(true);
        assertEquals(1, activeLoans.size());
    }

    @Test
    void findByBookSetIdAndActive() {
        List<LoanDoc> activeLoans = loanRepository.findByBookSetIdAndActive(book1Id, true);
        assertEquals(1, activeLoans.size());
    }

    @Test
    void findByClientIdAndActive() {
        List<LoanDoc> loans = loanRepository.findByClientIdAndActive(client2Id, false);
        assertEquals(1, loans.size());
    }
}