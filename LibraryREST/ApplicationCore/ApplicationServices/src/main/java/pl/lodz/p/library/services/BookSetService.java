package pl.lodz.p.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.library.domain.exceptions.BookSetException;
import pl.lodz.p.library.domain.model.BookSet;
import pl.lodz.p.library.ports.inbound.BookSetUseCase;
import pl.lodz.p.library.ports.outbound.DeleteBookSetPort;
import pl.lodz.p.library.ports.outbound.GetBookSetPort;
import pl.lodz.p.library.ports.outbound.SaveBookSetPort;

import java.util.List;

@Service
public class BookSetService implements BookSetUseCase {
    private final GetBookSetPort getBookSetPort;
    private final SaveBookSetPort saveBookSetPort;
    private final DeleteBookSetPort deleteBookSetPort;

    @Autowired
    public BookSetService(GetBookSetPort getBookSetPort, SaveBookSetPort saveBookSetPort, DeleteBookSetPort deleteBookSetPort) {
        this.getBookSetPort = getBookSetPort;
        this.saveBookSetPort = saveBookSetPort;
        this.deleteBookSetPort = deleteBookSetPort;
    }

    public BookSet findBookSetById(String id) {
        return getBookSetPort.findById(id)
                .orElseThrow(() -> new BookSetException("Książka o ID: " + id + " nie znaleziona."));
    }

    public List<BookSet> findBookSetsByTitle(String title) {
        return getBookSetPort.findBookSetsByTitle(title);
    }

    public List<BookSet> findBookSetsByAuthor(String author) {
        return getBookSetPort.findBookSetsByAuthor(author);
    }

    public List<BookSet> findBookSetsByReleaseYear(int releaseYear) {
        return getBookSetPort.findBookSetsByReleaseYear(releaseYear);
    }

    public List<BookSet> findAllBookSetsByQuantity(int quantity) {
        return getBookSetPort.findAllBookSetsByQuantity(quantity);
    }

    public List<BookSet> findBookSetsByAvailable(boolean available) {
        if (available) {
            return getBookSetPort.findByQuantityGreaterThan(0);
        } else {
            return getBookSetPort.findBookSetsByQuantity(0);
        }
    }

    public List<BookSet> findAllBookSets() {
        return getBookSetPort.findAllBookSets();
    }

    @Transactional
    public BookSet addBookSet(BookSet bookSet) {
        return saveBookSetPort.save(bookSet);
    }

    @Transactional
    public BookSet updateBookSet(String id, BookSet bookSetUpdates) {
        BookSet existingBookSet = findBookSetById(id);
        existingBookSet.setQuantity(bookSetUpdates.getQuantity());
        return saveBookSetPort.save(existingBookSet);
    }

    @Transactional
    public void deleteBookSet(String id) {
        deleteBookSetPort.deleteBookSet(id);
    }
}