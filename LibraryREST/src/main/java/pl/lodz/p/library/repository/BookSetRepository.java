package pl.lodz.p.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.model.BookSet;

import java.util.List;

@Repository
public interface BookSetRepository extends MongoRepository<BookSet, String> {
    List<BookSet> findBookByTitle(String title);

    List<BookSet> findBooksByAuthor(String author);

    List<BookSet> findBooksByReleaseYear(int releaseYear);

    List<BookSet> findBooksByQuantity(int quantity);

    List<BookSet> findByQuantityGreaterThan(int quantity);
}
