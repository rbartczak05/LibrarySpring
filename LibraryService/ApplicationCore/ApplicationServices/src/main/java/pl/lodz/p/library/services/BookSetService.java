package pl.lodz.p.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;
import pl.lodz.p.library.ports.outbound.BookSetPort;
import pl.lodz.p.library.ports.outbound.LoanPort;

import java.util.List;
import java.util.UUID;

@Service
public class BookSetService implements BookSetUseCase {
    private final BookSetPort bookSetPort;
    private final LoanPort loanPort;

    @Autowired
    public BookSetService(BookSetPort bookSetPort, LoanPort loanPort) {
        this.bookSetPort = bookSetPort;
        this.loanPort = loanPort;
    }

    public BookSet findBookSetById(UUID id) {
        return bookSetPort.findById(id)
                .orElseThrow(() -> new BookSetException("Książka o ID: " + id + " nie znaleziona."));
    }

    public List<BookSet> findBookSetsByTitle(String title) {
        return bookSetPort.findBookSetsByTitle(title);
    }

    public List<BookSet> findBookSetsByAuthor(String author) {
        return bookSetPort.findBookSetsByAuthor(author);
    }

    public List<BookSet> findBookSetsByReleaseYear(int releaseYear) {
        return bookSetPort.findBookSetsByReleaseYear(releaseYear);
    }

    public List<BookSet> findAllBookSetsByQuantity(int quantity) {
        return bookSetPort.findAllBookSetsByQuantity(quantity);
    }

    public List<BookSet> findBookSetsByAvailable(boolean available) {
        if (available) {
            return bookSetPort.findByQuantityGreaterThan(0);
        } else {
            return bookSetPort.findBookSetsByQuantity(0);
        }
    }

    public List<BookSet> findAllBookSets() {
        return bookSetPort.findAllBookSets();
    }

    @Transactional
    public BookSet addBookSet(BookSet bookSet) {
        return bookSetPort.save(bookSet);
    }

    @Transactional
    public BookSet updateBookSet(UUID id, BookSet bookSetUpdates) {
        BookSet existingBookSet = findBookSetById(id);
        existingBookSet.setTitle(bookSetUpdates.getTitle());
        existingBookSet.setAuthor(bookSetUpdates.getAuthor());
        existingBookSet.setReleaseYear(bookSetUpdates.getReleaseYear());
        existingBookSet.setQuantity(bookSetUpdates.getQuantity());
        return bookSetPort.save(existingBookSet);
    }

    @Transactional
    public void deleteBookSet(UUID id) {
        if (!loanPort.findByBookSetIdAndActive(id, true).isEmpty()) {
            throw new BookSetException("Nie można usunąć książki, która jest obecnie wypożyczona.");
        }
        bookSetPort.deleteBookSet(id);
    }
}