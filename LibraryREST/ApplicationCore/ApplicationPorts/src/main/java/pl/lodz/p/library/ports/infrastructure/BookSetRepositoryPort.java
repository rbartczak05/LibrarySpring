package pl.lodz.p.library.ports.infrastructure;

import pl.lodz.p.library.domain.model.BookSet;
import java.util.List;
import java.util.Optional;

public interface BookSetRepositoryPort {
    List<BookSet> findAll();
    Optional<BookSet> findBookSetById(String id);
    List<BookSet> findBookSetsByTitle(String title);
    List<BookSet> findBookSetsByAuthor(String author);
    List<BookSet> findBookSetsByReleaseYear(int releaseYear);
    List<BookSet> findBookSetsByQuantity(int quantity);
    List<BookSet> findAllBookSetsByQuantity(int quantity);
    List<BookSet> findBookSetsByAvailable(boolean available);
    List<BookSet> findAllBookSets();
    Optional<BookSet> addBookSet(BookSet bookSet);
    Optional<BookSet> updateBookSet(String id, BookSet bookSetUpdates);
    void deleteBookSet(String id);
    BookSet save(BookSet bookSet);
    List<BookSet> findByQuantityGreaterThan(int quantity);
}
