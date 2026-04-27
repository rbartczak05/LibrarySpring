package pl.lodz.p.library.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.exceptions.ClientException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.domain.model.Client;
import pl.lodz.p.library.domain.model.Loan;

class LoanServiceTest extends BaseServiceTest {

    @Autowired
    private LoanService loanService;

    private Client client1;
    private BookSet book1;

    @BeforeEach
    void setUp() {
        super.cleanUp();

        client1 = new Client("Jan", "Kowalski", "jan@mail.com", 20);
        client1.setActive(true);
        client1 = clientPort.save(client1);

        book1 = new BookSet("book", "author", 2000, 1);
        book1 = bookSetPort.save(book1);
    }

    @Test
    void createLoanTest() {
        Loan loan = loanService.createLoan(client1.getId(), book1.getId());

        Assertions.assertNotNull(loan.getId());
        Assertions.assertTrue(loan.isActive());
        Assertions.assertEquals(client1.getId(), loan.getClientId());

        Client updatedClient = clientPort.findById(client1.getId()).orElseThrow();
        BookSet updatedBook = bookSetPort.findById(book1.getId()).orElseThrow();

        Assertions.assertEquals(1, updatedClient.getCurrentLoansCount());
        Assertions.assertEquals(0, updatedBook.getQuantity());
    }

    @Test
    void createLoanFailClientInactiveTest() {
        Client clientInactive = new Client("Anna", "Nowak", "anna@mail.com", 20);
        clientInactive.setActive(false);
        clientInactive = clientPort.save(clientInactive);

        Client finalClient = clientInactive;
        Assertions.assertThrows(ClientException.class, () -> loanService.createLoan(finalClient.getId(), book1.getId()));
    }

    @Test
    void createLoanFailBookUnavailableTest() {
        BookSet bookUnavailable = new BookSet("bookUnavailable", "author", 2000, 0);
        bookUnavailable = bookSetPort.save(bookUnavailable);

        BookSet finalBook = bookUnavailable;
        Assertions.assertThrows(BookSetException.class, () -> loanService.createLoan(client1.getId(), finalBook.getId()));
    }

    @Test
    void endLoanTest() {
        Loan savedLoan = loanService.createLoan(client1.getId(), book1.getId());

        loanService.endLoan(savedLoan.getId());

        Client updatedClient = clientPort.findById(client1.getId()).orElseThrow();
        BookSet updatedBook = bookSetPort.findById(book1.getId()).orElseThrow();

        Assertions.assertEquals(0, updatedClient.getCurrentLoansCount());
        Assertions.assertEquals(1, updatedBook.getQuantity());
        Assertions.assertFalse(loanService.findLoanById(savedLoan.getId()).isActive());
    }
}