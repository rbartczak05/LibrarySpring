package pl.lodz.p.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.exception.*;
import pl.lodz.p.library.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

class ModelTest {

    @Test
    void readerConstructorTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        Assertions.assertEquals("testUser", reader.getLogin());
        Assertions.assertEquals("test@gmail.com", reader.getEmail());
        Assertions.assertEquals(25, reader.getAge());
        Assertions.assertFalse(reader.isActive());
        Assertions.assertEquals(5, reader.getMaxLoans());
        Assertions.assertEquals(0, reader.getCurrentLoansCount());
    }

    @Test
    void userHasInvalidFieldValuesTest() {
        Assertions.assertThrows(UserHasInvalidFieldValueException.class,
                () -> new Reader(null, "test@gmail.com", 12));

        Reader reader = new Reader("testReader", "test@example.com", 10);
        Assertions.assertThrows(UserHasInvalidFieldValueException.class, () -> reader.setLogin(null));
        Assertions.assertThrows(UserHasInvalidFieldValueException.class, () -> reader.setLogin(""));
        Assertions.assertThrows(UserHasInvalidFieldValueException.class, () -> reader.setEmail(""));
        Assertions.assertThrows(UserHasInvalidFieldValueException.class, () -> reader.setEmail(null));
        Assertions.assertThrows(UserHasInvalidFieldValueException.class, () -> reader.setAge(-5));
    }

    @Test
    void readerCanBorrowBookTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        reader.setActive(true);

        reader.setCurrentLoansCount(4);
        Assertions.assertTrue(reader.canBorrowBook());

        reader.setCurrentLoansCount(5);
        Assertions.assertThrows(ReaderLimitsException.class, reader::canBorrowBook);

        reader.setActive(false);
        reader.setCurrentLoansCount(2);
        Assertions.assertThrows(ReaderIsInactiveException.class, reader::canBorrowBook);
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
    void bookSetHasInvalidFieldValuesTest() {
        Assertions.assertThrows(BookSetHasInvalidFieldValueException.class,
                () -> new BookSet(null, "Frank Herbert", 1965, 3));
        Assertions.assertThrows(BookSetHasInvalidFieldValueException.class,
                () -> new BookSet("Diuna", null, 1965, 3));
        Assertions.assertThrows(BookSetHasInvalidFieldValueException.class,
                () -> new BookSet("Diuna", "Frank Herbert", -1965, 3));

    }

    @Test
    void bookSetIsAvailableTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 1);
        Assertions.assertTrue(book.isAvailable());

        book.setQuantity(0);
        Assertions.assertFalse(book.isAvailable());
    }

    @Test
    void bookSetSetQuantityFailTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 1);
        Assertions.assertThrows(BookSetQuantityException.class, () -> book.setQuantity(-1));
    }

    @Test
    void loanConstructorTest() {
        Reader reader = new Reader("testUser", "test@gmail.com", 25);
        reader.setId(UUID.randomUUID().toString());
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 3);
        book.setId(UUID.randomUUID().toString());

        LocalDateTime before = LocalDateTime.now();
        Loan loan = new Loan(reader.getId(), book.getId());
        LocalDateTime after = LocalDateTime.now();

        Assertions.assertEquals(reader.getId(), loan.getReaderId());
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

        Assertions.assertThrows(BookSetTimeException.class, () -> loan.setReturnTime(beforeStart));
    }

    @Test
    void loanSetEndTimeFailTest() {
        Loan loan = new Loan(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        LocalDateTime startTime = loan.getStartTime();
        LocalDateTime beforeStart = startTime.minusDays(1);

        Assertions.assertThrows(BookSetTimeException.class, () -> loan.setEndTime(beforeStart));
    }
}