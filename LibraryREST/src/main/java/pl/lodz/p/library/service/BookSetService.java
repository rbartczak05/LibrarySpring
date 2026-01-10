package pl.lodz.p.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.exception.BookSetNotAvailableException;
import pl.lodz.p.library.exception.BookSetNotFoundException;
import pl.lodz.p.library.exception.BookSetTitleException;
import pl.lodz.p.library.exception.IdException;
import pl.lodz.p.library.model.BookSet;
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
                .orElseThrow(() -> new BookSetNotFoundException(HttpStatus.NOT_FOUND, "BookSet with id: " + id + " not found"));
    }

    public List<BookSet> findBookSetsByTitle(String title) {
        return bookSetRepository.findBookByTitle(title);
    }

    public List<BookSet> findBookSetByAuthor(String author) {
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
        if (bookSet.getId() != null && bookSetRepository.findById(bookSet.getId()).isPresent()) {
            throw new IdException(HttpStatus.CONFLICT, "BookSet with ID: " + bookSet.getId() + " already exists");
        }
        if (bookSet.getTitle() == null || "".equals(bookSet.getTitle())) {
            throw new BookSetTitleException(HttpStatus.CONFLICT, "BookSet title cannot be empty");
        }
        return bookSetRepository.save(bookSet);
    }

    @Transactional
    public BookSet updateBookSet(String id, BookSet bookSetUpdates) {
        if (bookSetUpdates == null) {
            throw new BookSetNotFoundException(HttpStatus.NOT_FOUND, "BookSetUpdates not found");
        }

        BookSet existingBookSet = findBookSetById(id);

        if (existingBookSet == null) {
            throw new BookSetNotFoundException(HttpStatus.NOT_FOUND, "BookSet with id: " + id + " not found");
        }

        existingBookSet.setQuantity(bookSetUpdates.getQuantity());

        return bookSetRepository.save(existingBookSet);
    }

    @Transactional
    public void deleteBookSet(String id) {
        BookSet bookSet = findBookSetById(id);

        if (bookSet == null) {
            throw new BookSetNotFoundException(HttpStatus.NOT_FOUND, "BookSet with id: " + id + " not found");
        }

        if (loanRepository.findByBookSetIdAndActive(id, true).size() > 0) {
            throw new BookSetNotAvailableException(HttpStatus.BAD_REQUEST, "Cannot delete BookSet with id: " + id + ". It has active loans.");
        }

        bookSetRepository.delete(bookSet);
    }
}