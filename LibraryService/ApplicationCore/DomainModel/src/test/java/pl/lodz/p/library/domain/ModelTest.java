package pl.lodz.p.library.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.exceptions.ClientException;
import pl.lodz.p.library.domain.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

class ModelTest {

    @Test
    void clientConstructorTest() {
        Client client = new Client("Jan", "Kowalski", "test@gmail.com", 25);

        Assertions.assertEquals("Jan", client.getFirstName());
        Assertions.assertEquals("Kowalski", client.getLastName());
        Assertions.assertEquals("test@gmail.com", client.getEmail());
        Assertions.assertEquals(25, client.getAge());
        Assertions.assertFalse(client.isActive());
        Assertions.assertEquals(5, client.getMaxLoans());
        Assertions.assertEquals(0, client.getCurrentLoansCount());
    }

    @Test
    void clientCanBorrowBookTest() {
        Client client = new Client("Jan", "Kowalski", "test@gmail.com", 25);
        client.setActive(true);

        client.setCurrentLoansCount(4);
        Assertions.assertTrue(client.canBorrowBook());

        client.setCurrentLoansCount(5);
        Assertions.assertThrows(ClientException.class, client::canBorrowBook);

        client.setActive(false);
        client.setCurrentLoansCount(0);
        Assertions.assertThrows(ClientException.class, client::canBorrowBook);
    }

    @Test
    void loanConstructorTest() {
        Client client = new Client("Jan", "Kowalski", "test@gmail.com", 25);
        client.setId(UUID.randomUUID().toString());

        BookSet book = new BookSet("Testowy Tytuł", "Testowy Autor", 2020, 10);
        book.setId(UUID.randomUUID().toString());

        LocalDateTime before = LocalDateTime.now();
        Loan loan = new Loan(client.getId(), book.getId());
        LocalDateTime after = LocalDateTime.now();

        Assertions.assertEquals(client.getId(), loan.getClientId());
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