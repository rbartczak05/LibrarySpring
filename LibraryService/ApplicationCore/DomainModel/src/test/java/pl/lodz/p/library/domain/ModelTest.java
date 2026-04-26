package pl.lodz.p.library.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.exceptions.UserException;
import pl.lodz.p.library.domain.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

class ModelTest {

    @Test
    void readerConstructorTest() {
        Client client = new Client("testUser", "test@gmail.com", 25);
        Assertions.assertEquals("testUser", client.getLogin());
        Assertions.assertEquals("test@gmail.com", client.getEmail());
        Assertions.assertEquals(25, client.getAge());
        Assertions.assertFalse(client.isActive());
        Assertions.assertEquals(5, client.getMaxLoans());
        Assertions.assertEquals(0, client.getCurrentLoansCount());
    }

    @Test
    void readerCanBorrowBookTest() {
        Client client = new Client("testUser", "test@gmail.com", 25);
        client.setActive(true);

        client.setCurrentLoansCount(4);
        Assertions.assertTrue(client.canBorrowBook());

        client.setCurrentLoansCount(5);
        Assertions.assertThrows(UserException.class, client::canBorrowBook);

        client.setActive(false);
        client.setCurrentLoansCount(2);
        Assertions.assertThrows(UserException.class, client::canBorrowBook);
    }

    @Test
    void administratorConstructorTest() {
        Administrator admin = new Administrator("adminUser", "admin@gmail.com", 40);
        Assertions.assertEquals("adminUser", admin.getLogin());
        Assertions.assertEquals("admin@gmail.com", admin.getEmail());
        Assertions.assertEquals(40, admin.getAge());
        Assertions.assertTrue(admin.isActive());
    }

    @Test
    void librarianConstructorTest() {
        Librarian librarian = new Librarian("libUser", "lib@gmail.com", 35);
        Assertions.assertEquals("libUser", librarian.getLogin());
        Assertions.assertEquals("lib@gmail.com", librarian.getEmail());
        Assertions.assertEquals(35, librarian.getAge());
        Assertions.assertTrue(librarian.isActive());
    }

    @Test
    void bookSetConstructorTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 3);
        Assertions.assertEquals("Diuna", book.getTitle());
        Assertions.assertEquals("Frank Herbert", book.getAuthor());
        Assertions.assertEquals(1965, book.getReleaseYear());
        Assertions.assertEquals(3, book.getQuantity());
    }

    @Test
    void bookSetIsAvailableTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 1);
        Assertions.assertTrue(book.isAvailable());

        book.setQuantity(0);
        Assertions.assertFalse(book.isAvailable());
    }

    @Test
    void loanConstructorTest() {
        Client client = new Client("testUser", "test@gmail.com", 25);
        client.setId(UUID.randomUUID().toString());
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 3);
        book.setId(UUID.randomUUID().toString());

        LocalDateTime before = LocalDateTime.now();
        Loan loan = new Loan(client.getId(), book.getId());
        LocalDateTime after = LocalDateTime.now();

        Assertions.assertEquals(client.getId(), loan.getReaderId());
        Assertions.assertEquals(book.getId(), loan.getBookSetId());
        Assertions.assertTrue(loan.isActive());

        Assertions.assertNull(loan.getReturnTime());
        Assertions.assertNotNull(loan.getStartTime());
        Assertions.assertNotNull(loan.getEndTime());

        Assertions.assertFalse(loan.getStartTime().isBefore(before));
        Assertions.assertFalse(loan.getStartTime().isAfter(after));

        Assertions.assertFalse(loan.getEndTime().isBefore(before));
        Assertions.assertTrue(loan.getEndTime().isAfter(loan.getStartTime()));
        Assertions.assertTrue(loan.getEndTime().isAfter(after));
    }

    @Test
    void loanSetReturnTimeFailTest() {
        Loan loan = new Loan(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        LocalDateTime startTime = loan.getStartTime();
        LocalDateTime beforeStart = startTime.minusDays(1);

        Assertions.assertThrows(BookSetException.class, () -> loan.setReturnTime(beforeStart));
    }

    @Test
    void loanSetEndTimeFailTest() {
        Loan loan = new Loan(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        LocalDateTime startTime = loan.getStartTime();
        LocalDateTime beforeStart = startTime.minusDays(1);

        Assertions.assertThrows(BookSetException.class, () -> loan.setEndTime(beforeStart));
    }
}