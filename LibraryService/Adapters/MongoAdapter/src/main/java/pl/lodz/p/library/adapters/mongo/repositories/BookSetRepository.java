package pl.lodz.p.library.adapters.mongo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;

import java.util.List;

@Repository
public interface BookSetRepository extends MongoRepository<BookSetDoc, String> {
    List<BookSetDoc> findBookByTitle(String title);

    List<BookSetDoc> findBooksByAuthor(String author);

    List<BookSetDoc> findBooksByReleaseYear(int releaseYear);

    List<BookSetDoc> findBooksByQuantity(int quantity);

    List<BookSetDoc> findByQuantityGreaterThan(int quantity);
}