package pl.lodz.p.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.library.exception.*;
import pl.lodz.p.library.model.BookSet;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.model.Reader;
import pl.lodz.p.library.repository.BookSetRepository;
import pl.lodz.p.library.repository.LoanRepository;
import pl.lodz.p.library.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Testcontainers
@SpringBootTest
class LoanServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookSetRepository bookSetRepository;
    private LoanService loanService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookSetService bookSetService;

    private Reader reader1;
    private BookSet book1;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        loanService = new LoanService(loanRepository, bookSetRepository, userRepository, userService, bookSetService);

        loanRepository.deleteAll();
        userRepository.deleteAll();
        bookSetRepository.deleteAll();

        reader1 = new Reader("reader", "reader@mail.com", 20);
        reader1.setActive(true);
        userRepository.save(reader1);

        book1 = new BookSet("book", "author", 2000, 1);
        bookSetRepository.save(book1);
    }

    @Test
    void createLoanTest() {
        Assertions.assertEquals(0, reader1.getCurrentLoansCount());
        Assertions.assertEquals(1, book1.getQuantity());
        Assertions.assertTrue(book1.isAvailable());

        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());

        Assertions.assertNotNull(loan.getId());
        Assertions.assertTrue(loan.isActive());
        Assertions.assertEquals(reader1.getId(), loan.getReaderId());

        Reader updatedReader = (Reader) userService.findUserById(reader1.getId());
        BookSet updatedBook = bookSetService.findBookSetById(book1.getId());

        Assertions.assertEquals(1, updatedReader.getCurrentLoansCount());
        Assertions.assertEquals(0, updatedBook.getQuantity());
        Assertions.assertFalse(updatedBook.isAvailable());
    }

    @Test
    void createLoanWithStartTimeTest() {
        LocalDateTime futureTime = LocalDateTime.now().plusDays(10);
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId(), futureTime);

        Assertions.assertEquals(futureTime, loan.getStartTime());
        Assertions.assertEquals(futureTime.plusDays(30), loan.getEndTime());
    }

    @Test
    void createLoanFailReaderInactiveTest() {
        Reader readerInactive = new Reader("readerInactive", "readerInactive@mail.com", 20);
        userRepository.save(readerInactive);
        Assertions.assertThrows(ReaderIsInactiveException.class, () -> loanService.createLoan(readerInactive.getId(), book1.getId()));
    }

    @Test
    void createLoanFailBookUnavailableTest() {
        BookSet bookUnavailable = new BookSet("bookUnavailable", "author", 2000, 0);
        bookSetRepository.save(bookUnavailable);
        Assertions.assertThrows(BookSetNotAvailableException.class, () -> loanService.createLoan(reader1.getId(), bookUnavailable.getId()));
    }

    @Test
    void createLoanFailReaderMaxLoansTest() {
        reader1.setCurrentLoansCount(5);
        userRepository.save(reader1);
        Assertions.assertThrows(ReaderLimitsException.class, () -> loanService.createLoan(reader1.getId(), book1.getId()));
    }

    @Test
    void createLoanFailResourceAlreadyAllocatedTest() {
        BookSet rareBook = new BookSet("Rare Book", "Famous Author", 1999, 1);
        bookSetRepository.save(rareBook);

        Reader reader2 = new Reader("reader2", "reader2@mail.com", 25);
        reader2.setActive(true);
        userRepository.save(reader2);

        loanService.createLoan(reader1.getId(), rareBook.getId());

        BookSet bookAfterFirstLoan = bookSetService.findBookSetById(rareBook.getId());
        Assertions.assertEquals(0, bookAfterFirstLoan.getQuantity());
        Assertions.assertFalse(bookAfterFirstLoan.isAvailable());

        Assertions.assertThrows(BookSetNotAvailableException.class, () -> loanService.createLoan(reader2.getId(), rareBook.getId()));
    }
    @Test
    void findAllLoansTest() {
        Reader reader2 = new Reader("reader2", "reader2@mail.com", 20);
        reader2.setActive(true);
        userRepository.save(reader2);
        BookSet book2 = new BookSet("book2", "author2", 2000, 2);
        bookSetRepository.save(book2);

        loanService.createLoan(reader1.getId(), book1.getId());
        loanService.createLoan(reader2.getId(), book2.getId());

        List<Loan> loans = loanService.findAllLoans();
        Assertions.assertEquals(2, loans.size());
    }

    @Test
    void findLoanByIdTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());
        Loan found = loanService.findLoanById(loan.getId());
        Assertions.assertEquals(loan.getId(), found.getId());
    }

    @Test
    void findLoanByIdFailTest() {
        Assertions.assertThrows(LoanNotFoundException.class, () -> loanService.findLoanById(UUID.randomUUID().toString()));
    }

    @Test
    void findLoansByReaderTest() {
        loanService.createLoan(reader1.getId(), book1.getId());
        List<Loan> loans = loanService.findLoansByReader(reader1.getId());
        Assertions.assertEquals(1, loans.size());
    }

    @Test
    void findLoansByBookSetTest() {
        loanService.createLoan(reader1.getId(), book1.getId());
        List<Loan> loans = loanService.findLoansByBookSet(book1.getId());
        Assertions.assertEquals(1, loans.size());
    }

    @Test
    void findLoansByReaderIdAndBookSetIdTest() {
        loanService.createLoan(reader1.getId(), book1.getId());
        List<Loan> loans = loanService.findLoansByReaderIdAndBookSetId(reader1.getId(), book1.getId());
        Assertions.assertEquals(1, loans.size());
    }

    @Test
    void findByActiveLoansTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());
        List<Loan> active = loanService.findByActiveLoans(true);
        Assertions.assertEquals(1, active.size());
        loanService.endLoan(loan.getId());
        List<Loan> inactive = loanService.findByActiveLoans(false);
        Assertions.assertEquals(1, inactive.size());
    }

    @Test
    void findByReaderIdAndActiveTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());
        List<Loan> active = loanService.findByReaderIdAndActive(reader1.getId(), true);
        Assertions.assertEquals(1, active.size());
        loanService.endLoan(loan.getId());
        List<Loan> inactive = loanService.findByReaderIdAndActive(reader1.getId(), false);
        Assertions.assertEquals(1, inactive.size());
    }

    @Test
    void findByBookSetIdAndActiveTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());
        List<Loan> active = loanService.findByBookSetIdAndActive(book1.getId(), true);
        Assertions.assertEquals(1, active.size());
        loanService.endLoan(loan.getId());
        List<Loan> inactive = loanService.findByBookSetIdAndActive(book1.getId(), false);
        Assertions.assertEquals(1, inactive.size());
    }

    @Test
    void updateLoanTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());
        LocalDateTime newStartTime = LocalDateTime.now().plusDays(1);
        LocalDateTime newEndTime = LocalDateTime.now().plusDays(31);
        Loan updates = new Loan();
        updates.setStartTime(newStartTime);
        updates.setEndTime(newEndTime);

        Loan updated = loanService.updateLoan(loan.getId(), updates);

        Assertions.assertEquals(newStartTime, updated.getStartTime());
        Assertions.assertEquals(newEndTime, updated.getEndTime());
    }

    @Test
    void updateLoanFailNotFoundTest() {
        Assertions.assertThrows(LoanNotFoundException.class, () -> loanService.updateLoan(UUID.randomUUID().toString(), new Loan()));
    }

    @Test
    void updateLoanFailAlreadyInactiveTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());
        loanService.endLoan(loan.getId());
        Assertions.assertThrows(LoanAlreadyInactiveException.class, () -> loanService.updateLoan(loan.getId(), new Loan()));
    }

    @Test
    void endLoanTest() {
        Loan savedLoan = loanService.createLoan(reader1.getId(), book1.getId());
        LocalDateTime beforeEnd = LocalDateTime.now();
        Loan endedLoan = loanService.endLoan(savedLoan.getId());

        Assertions.assertFalse(endedLoan.isActive());
        Assertions.assertNotNull(endedLoan.getReturnTime());
        Assertions.assertFalse(endedLoan.getReturnTime().isBefore(beforeEnd));

        Reader updatedReader = (Reader) userRepository.findById(reader1.getId()).orElseThrow();
        BookSet updatedBook = bookSetRepository.findById(book1.getId()).orElseThrow();

        Assertions.assertEquals(0, updatedReader.getCurrentLoansCount());
        Assertions.assertEquals(1, updatedBook.getQuantity());
    }

    @Test
    void endLoanFailNotFoundTest() {
        Assertions.assertThrows(LoanNotFoundException.class, () -> loanService.endLoan(UUID.randomUUID().toString()));
    }

    @Test
    void endLoanFailAlreadyEndedTest() {
        Loan endedLoan = loanService.createLoan(reader1.getId(), book1.getId());
        endedLoan.setActive(false);
        loanRepository.save(endedLoan);

        Assertions.assertThrows(LoanAlreadyInactiveException.class, () -> loanService.endLoan(endedLoan.getId()));
    }
}