package pl.lodz.p.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.exception.BookSetNotAvailableException;
import pl.lodz.p.library.exception.BookSetNotFoundException;
import pl.lodz.p.library.model.BookSet;
import pl.lodz.p.library.model.Loan;
import pl.lodz.p.library.repository.BookSetRepository;
import pl.lodz.p.library.repository.LoanRepository;

import java.util.List;

@Service
public class BookSetService {
    private final BookSetRepository bookSetRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public BookSetService(BookSetRepository bookSetRepository, LoanRepository loanRepository) {
        this.bookSetRepository = bookSetRepository;
        this.loanRepository = loanRepository;
    }

    public BookSet findBookSetById(String id) {
        return bookSetRepository.findById(id)
                .orElseThrow(() -> new BookSetNotFoundException(HttpStatus.NOT_FOUND, "Książka o ID: " + id + " nie znaleziona."));
    }

    public List<BookSet> findBookSetsByTitle(String title) {
        return bookSetRepository.findBookByTitle(title);
    }

    public List<BookSet> findBookSetsByAuthor(String author) {
        return bookSetRepository.findBooksByAuthor(author);
    }

    public List<BookSet> findBookSetsByReleaseYear(int releaseYear) {
        return bookSetRepository.findBooksByReleaseYear(releaseYear);
    }

    public List<BookSet> findAllBookSetsByQuantity(int quantity) {
        return bookSetRepository.findBooksByQuantity(quantity);
    }

    public List<BookSet> findBookSetsByAvailable(boolean available) {
        if (available) {
            return bookSetRepository.findByQuantityGreaterThan(0);
        } else {
            return bookSetRepository.findBooksByQuantity(0);
        }
    }

    public List<BookSet> findAllBookSets() {
        return bookSetRepository.findAll();
    }

    @Transactional
    public BookSet addBookSet(BookSet bookSet) {
        return bookSetRepository.save(bookSet);
    }

    @Transactional
    public BookSet updateBookSet(String id, BookSet bookSetUpdates) {
        BookSet existingBookSet = findBookSetById(id);

        existingBookSet.setQuantity(bookSetUpdates.getQuantity());

        return bookSetRepository.save(existingBookSet);
    }

    @Transactional
    public void deleteBookSet(String id) {
        BookSet bookSet = findBookSetById(id);

        List<Loan> activeLoans = loanRepository.findByBookSetIdAndActive(id, true);
        if (!activeLoans.isEmpty()) {
            throw new BookSetNotAvailableException(HttpStatus.CONFLICT, "Nie można usunąć książki o ID: " + id + ", ponieważ posiada aktywne wypożyczenia.");
        }

        bookSetRepository.delete(bookSet);
    }
}