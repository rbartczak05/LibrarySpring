package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.BookSet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookSetPort {
    List<BookSet> findAll();

    Optional<BookSet> findById(UUID id);

    List<BookSet> findBookSetsByTitle(String title);

    List<BookSet> findBookSetsByAuthor(String author);

    List<BookSet> findBookSetsByReleaseYear(int releaseYear);

    List<BookSet> findBookSetsByQuantity(int quantity);

    List<BookSet> findAllBookSetsByQuantity(int quantity);

    List<BookSet> findByQuantityGreaterThan(int quantity);

    List<BookSet> findByQuantityLessThan(int quantity);

    List<BookSet> findBookSetsByAvailable(boolean available);

    List<BookSet> findAllBookSets();

    Optional<BookSet> addBookSet(BookSet bookSet);

    Optional<BookSet> updateBookSet(UUID id, BookSet bookSetUpdates);

    BookSet save(BookSet bookSet);

    void deleteBookSet(UUID id);

    void deleteAll();
}