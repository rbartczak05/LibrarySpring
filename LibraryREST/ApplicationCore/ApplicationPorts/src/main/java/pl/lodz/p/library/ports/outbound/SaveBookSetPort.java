package pl.lodz.p.library.ports.outbound;

import pl.lodz.p.library.domain.model.BookSet;

import java.util.Optional;

public interface SaveBookSetPort {
    Optional<BookSet> addBookSet(BookSet bookSet);

    Optional<BookSet> updateBookSet(String id, BookSet bookSetUpdates);

    BookSet save(BookSet bookSet);
}
