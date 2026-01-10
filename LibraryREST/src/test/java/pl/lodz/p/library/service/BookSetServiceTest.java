package pl.lodz.p.library.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lodz.p.library.exception.BookSetNotAvailableException;
import pl.lodz.p.library.exception.BookSetNotFoundException;
import pl.lodz.p.library.exception.BookSetTitleException;
import pl.lodz.p.library.model.BookSet;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.model.Reader;

import java.util.List;
import java.util.UUID;

class BookSetServiceTest extends BaseServiceTest {

    private BookSetService bookSetService;

    @BeforeEach
    void setUp() {
        bookSetService = new BookSetService(bookSetRepository, loanRepository);
    }

    @Test
    void addBookSetTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        bookSetService.addBookSet(book);
        Assertions.assertEquals(1, bookSetRepository.count());
        Assertions.assertNotNull(book.getId());
    }

    @Test
    void addBookSetFailEmptyTitleTest() {
        BookSet book = new BookSet("", "Frank Herbert", 1965, 5);
        Assertions.assertThrows(BookSetTitleException.class, () -> bookSetService.addBookSet(book));
    }

    @Test
    void findAllBookSetsTest() {
        bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetRepository.save(new BookSet("Lalka", "Bolesław Prus", 1890, 2));
        List<BookSet> books = bookSetService.findAllBookSets();
        Assertions.assertEquals(2, books.size());
    }

    @Test
    void findBookSetByIdTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        BookSet savedBook = bookSetRepository.save(book);
        BookSet foundBook = bookSetService.findBookSetById(savedBook.getId());
        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals("Diuna", foundBook.getTitle());
    }

    @Test
    void findBookSetByIdFailNotFoundTest() {
        Assertions.assertThrows(BookSetNotFoundException.class, () -> bookSetService.findBookSetById(UUID.randomUUID().toString()));
    }

    @Test
    void findBookSetsByTitleTest() {
        bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetRepository.save(new BookSet("Diuna: Mesjasz", "Frank Herbert", 1969, 3));
        List<BookSet> books = bookSetService.findBookSetsByTitle("Diuna");
        Assertions.assertEquals(1, books.size());
    }

    @Test
    void findBookSetsByAuthorTest() {
        bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetRepository.save(new BookSet("Lalka", "Bolesław Prus", 1890, 2));
        List<BookSet> books = bookSetService.findBookSetsByAuthor("Frank Herbert");
        Assertions.assertEquals(1, books.size());
    }

    @Test
    void findBookSetsByReleaseYearTest() {
        bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetRepository.save(new BookSet("Rok 1984", "George Orwell", 1949, 1));
        List<BookSet> books = bookSetService.findBookSetsByReleaseYear(1965);
        Assertions.assertEquals(1, books.size());
    }

    @Test
    void findAllBookSetsByQuantityTest() {
        bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetRepository.save(new BookSet("Lalka", "Bolesław Prus", 1890, 5));
        List<BookSet> books = bookSetService.findAllBookSetsByQuantity(5);
        Assertions.assertEquals(2, books.size());
    }

    @Test
    void findBookSetsByAvailableTest() {
        bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetRepository.save(new BookSet("Lalka", "Bolesław Prus", 1890, 0));
        bookSetRepository.save(new BookSet("Rok 1984", "George Orwell", 1949, 1));

        List<BookSet> availableBooks = bookSetService.findBookSetsByAvailable(true);
        List<BookSet> unavailableBooks = bookSetService.findBookSetsByAvailable(false);

        Assertions.assertEquals(2, availableBooks.size());
        Assertions.assertEquals(1, unavailableBooks.size());
        Assertions.assertEquals("Lalka", unavailableBooks.getFirst().getTitle());
    }

    @Test
    void updateBookSetTest() {
        BookSet book = bookSetRepository.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        BookSet updates = new BookSet("NOWY TYTUŁ", "NOWY AUTOR", 2000, 100);

        BookSet updated = bookSetService.updateBookSet(book.getId(), updates);

        Assertions.assertEquals(100, updated.getQuantity());
        Assertions.assertEquals("Diuna", updated.getTitle());
    }

    @Test
    void updateBookSetFailNotFoundTest() {
        BookSet updates = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        Assertions.assertThrows(BookSetNotFoundException.class, () -> bookSetService.updateBookSet(UUID.randomUUID().toString(), updates));
    }

    @Test
    void deleteBookSetTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        BookSet savedBook = bookSetRepository.save(book);
        Assertions.assertEquals(1, bookSetRepository.count());

        bookSetService.deleteBookSet(savedBook.getId());

        Assertions.assertEquals(0, bookSetRepository.count());
    }

    @Test
    void deleteBookSetFailNotFoundTest() {
        Assertions.assertThrows(BookSetNotFoundException.class, () -> bookSetService.deleteBookSet(UUID.randomUUID().toString()));
    }

    @Test
    void deleteBookSetFailActiveLoanTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        Reader reader = new Reader("u1", "u1@mail.com", 20);
        userRepository.save(reader);
        BookSet savedBook = bookSetRepository.save(book);

        Loan loan = new Loan(reader.getId(), savedBook.getId());
        loan.setActive(true);
        loanRepository.save(loan);

        Assertions.assertThrows(BookSetNotAvailableException.class, () -> bookSetService.deleteBookSet(savedBook.getId()));

        Assertions.assertEquals(1, bookSetRepository.count());
    }
}