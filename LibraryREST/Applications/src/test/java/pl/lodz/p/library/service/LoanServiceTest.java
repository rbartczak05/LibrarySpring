package pl.lodz.p.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.exceptions.UserException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.domain.model.Reader;
import pl.lodz.p.library.services.LoanService;

class LoanServiceTest extends BaseServiceTest {

    @Autowired
    private LoanService loanService;

    private Reader reader1;
    private BookSet book1;

    @BeforeEach
    void setUp() {
        reader1 = new Reader("reader", "reader@mail.com", 20);
        reader1.setActive(true);
        reader1 = (Reader) userPort.addUser(reader1).orElseThrow();

        book1 = new BookSet("book", "author", 2000, 1);
        book1 = bookSetPort.save(book1);
    }

    @Test
    void createLoanTest() {
        Loan loan = loanService.createLoan(reader1.getId(), book1.getId());

        Assertions.assertNotNull(loan.getId());
        Assertions.assertTrue(loan.isActive());
        Assertions.assertEquals(reader1.getId(), loan.getReaderId());

        Reader updatedReader = (Reader) userPort.findUserById(reader1.getId()).orElseThrow();
        BookSet updatedBook = bookSetPort.findById(book1.getId()).orElseThrow();

        Assertions.assertEquals(1, updatedReader.getCurrentLoansCount());
        Assertions.assertEquals(0, updatedBook.getQuantity());
    }

    @Test
    void createLoanFailReaderInactiveTest() {
        Reader readerInactive = new Reader("readerInactive", "readerInactive@mail.com", 20);
        readerInactive.setActive(false);
        readerInactive = (Reader) userPort.addUser(readerInactive).orElseThrow();

        Reader finalReader = readerInactive;
        Assertions.assertThrows(UserException.class, () -> loanService.createLoan(finalReader.getId(), book1.getId()));
    }

    @Test
    void createLoanFailBookUnavailableTest() {
        BookSet bookUnavailable = new BookSet("bookUnavailable", "author", 2000, 0);
        bookUnavailable = bookSetPort.save(bookUnavailable);

        BookSet finalBook = bookUnavailable;
        Assertions.assertThrows(BookSetException.class, () -> loanService.createLoan(reader1.getId(), finalBook.getId()));
    }

    @Test
    void endLoanTest() {
        Loan savedLoan = loanService.createLoan(reader1.getId(), book1.getId());

        loanService.endLoan(savedLoan.getId());

        Reader updatedReader = (Reader) userPort.findUserById(reader1.getId()).orElseThrow();
        BookSet updatedBook = bookSetPort.findById(book1.getId()).orElseThrow();

        Assertions.assertEquals(0, updatedReader.getCurrentLoansCount());
        Assertions.assertEquals(1, updatedBook.getQuantity());
    }
}