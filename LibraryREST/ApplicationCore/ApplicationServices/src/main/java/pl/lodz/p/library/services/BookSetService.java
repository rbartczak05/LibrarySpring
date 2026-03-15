package pl.lodz.p.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.domain.exceptions.BookSetNotAvailableException;
import pl.lodz.p.library.domain.exceptions.BookSetNotFoundException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.domain.model.Loan;
import pl.lodz.p.library.ports.infrastructure.BookSetRepositoryPort;
import pl.lodz.p.library.ports.infrastructure.LoanRepositoryPort;

import java.util.List;

@Service
public class BookSetService {
    private final BookSetRepositoryPort bookSetRepositoryPort;
    private final LoanRepositoryPort loanRepositoryPort;

    @Autowired
    public BookSetService(BookSetRepositoryPort bookSetRepositoryPort, LoanRepositoryPort loanRepositoryPort) {
        this.bookSetRepositoryPort = bookSetRepositoryPort;
        this.loanRepositoryPort = loanRepositoryPort;
    }

    public BookSet findBookSetById(String id) {
        return bookSetRepositoryPort.findById(id)
                .orElseThrow(() -> new BookSetNotFoundException("Książka o ID: " + id + " nie znaleziona."));
    }

    public List<BookSet> findBookSetsByTitle(String title) {
        return bookSetRepositoryPort.findBookSetsByTitle(title);
    }

    public List<BookSet> findBookSetsByAuthor(String author) {
        return bookSetRepositoryPort.findBookSetsByAuthor(author);
    }

    public List<BookSet> findBookSetsByReleaseYear(int releaseYear) {
        return bookSetRepositoryPort.findBookSetsByReleaseYear(releaseYear);
    }

    public List<BookSet> findAllBookSetsByQuantity(int quantity) {
        return bookSetRepositoryPort.findBookSetsByQuantity(quantity);
    }

    public List<BookSet> findBookSetsByAvailable(boolean available) {
        if (available) {
            return bookSetRepositoryPort.findByQuantityGreaterThan(0);
        } else {
            return bookSetRepositoryPort.findBookSetsByQuantity(0);
        }
    }

    public List<BookSet> findAllBookSets() {
        return bookSetRepositoryPort.findAll();
    }

    @Transactional
    public BookSet addBookSet(BookSet bookSet) {
        return bookSetRepositoryPort.save(bookSet);
    }

    @Transactional
    public BookSet updateBookSet(String id, BookSet bookSetUpdates) {
        BookSet existingBookSet = findBookSetById(id);

        existingBookSet.setQuantity(bookSetUpdates.getQuantity());

        return bookSetRepositoryPort.save(existingBookSet);
    }

    @Transactional
    public void deleteBookSet(String id) {
        BookSet bookSet = findBookSetById(id);

        List<Loan> activeLoans = loanRepositoryPort.findByBookSetIdAndActive(id, true);
        if (!activeLoans.isEmpty()) {
            throw new BookSetNotAvailableException(HttpStatus.CONFLICT, "Nie można usunąć książki o ID: " + id + ", ponieważ posiada aktywne wypożyczenia.");
        }

        bookSetRepositoryPort.delete(bookSet);
    }
}