package pl.lodz.p.library.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.model.BookSet;

import java.util.List;
import java.util.UUID;

class BookSetServiceTest extends BaseServiceTest {

    @Autowired
    private BookSetService bookSetService;

    @Test
    void addBookSetTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        BookSet saved = bookSetService.addBookSet(book);

        Assertions.assertEquals(1, bookSetService.findAllBookSets().size());
        Assertions.assertNotNull(saved.getId());
    }

    @Test
    void findAllBookSetsTest() {
        bookSetPort.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        bookSetPort.save(new BookSet("Lalka", "Bolesław Prus", 1890, 2));

        List<BookSet> books = bookSetService.findAllBookSets();
        Assertions.assertEquals(2, books.size());
    }

    @Test
    void findBookSetByIdTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        BookSet savedBook = bookSetPort.save(book);

        BookSet foundBook = bookSetService.findBookSetById(savedBook.getId());

        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals("Diuna", foundBook.getTitle());
    }

    @Test
    void findBookSetByIdFailNotFoundTest() {
        Assertions.assertThrows(BookSetException.class, () -> bookSetService.findBookSetById(UUID.randomUUID()));
    }

    @Test
    void updateBookSetTest() {
        BookSet book = bookSetPort.save(new BookSet("Diuna", "Frank Herbert", 1965, 5));
        BookSet updates = new BookSet("NOWY TYTUŁ", "NOWY AUTOR", 2000, 100);

        BookSet updated = bookSetService.updateBookSet(book.getId(), updates);

        Assertions.assertEquals(100, updated.getQuantity());
        Assertions.assertEquals("Diuna", updated.getTitle());
    }

    @Test
    void deleteBookSetTest() {
        BookSet book = new BookSet("Diuna", "Frank Herbert", 1965, 5);
        BookSet savedBook = bookSetPort.save(book);
        Assertions.assertEquals(1, bookSetService.findAllBookSets().size());

        bookSetService.deleteBookSet(savedBook.getId());

        Assertions.assertEquals(0, bookSetService.findAllBookSets().size());
    }
}