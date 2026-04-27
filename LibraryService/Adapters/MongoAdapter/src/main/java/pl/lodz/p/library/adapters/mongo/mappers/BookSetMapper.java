package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.BookSetDoc;
import pl.lodz.p.library.domain.model.BookSet;

@Component
public class BookSetMapper {

    public BookSet toDomain(BookSetDoc doc) {
        if (doc == null) return null;

        BookSet bookSet = new BookSet(doc.getTitle(), doc.getAuthor(), doc.getReleaseYear(), doc.getQuantity());
        bookSet.setId(doc.getId());

        return bookSet;
    }

    public BookSetDoc toDocument(BookSet bookSet) {
        if (bookSet == null) return null;

        BookSetDoc doc = new BookSetDoc(bookSet.getTitle(), bookSet.getAuthor(), bookSet.getReleaseYear(), bookSet.getQuantity());
        doc.setId(bookSet.getId());

        return doc;
    }
}