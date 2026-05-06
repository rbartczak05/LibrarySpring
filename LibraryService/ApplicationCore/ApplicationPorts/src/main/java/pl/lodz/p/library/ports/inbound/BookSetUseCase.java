package pl.lodz.p.library.ports.inbound;

import pl.lodz.p.library.domain.model.BookSet;

import java.util.List;
import java.util.UUID;

public interface BookSetUseCase {
    BookSet findBookSetById(UUID id);

    List<BookSet> findBookSetsByTitle(String title);

    List<BookSet> findBookSetsByAuthor(String author);

    List<BookSet> findBookSetsByReleaseYear(int releaseYear);

    List<BookSet> findAllBookSetsByQuantity(int quantity);

    List<BookSet> findBookSetsByAvailable(boolean available);

    List<BookSet> findAllBookSets();

    BookSet addBookSet(BookSet bookSet);

    BookSet updateBookSet(UUID id, BookSet bookSetUpdates);

    void deleteBookSet(UUID id);
}