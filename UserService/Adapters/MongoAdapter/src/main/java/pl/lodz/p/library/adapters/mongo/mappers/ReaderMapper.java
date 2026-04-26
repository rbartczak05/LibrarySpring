package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.library.domain.model.Reader;

@Component
public class ReaderMapper {

    public Reader toDomain(ReaderDoc doc) {
        if (doc == null) return null;

        Reader reader = new Reader(doc.getLogin(), doc.getPassword(), doc.getEmail(), doc.getAge());
        reader.setId(doc.getId());
        reader.setActive(doc.isActive());
        reader.setCurrentLoansCount(doc.getCurrentLoansCount());

        return reader;
    }

    public ReaderDoc toDocument(Reader reader) {
        if (reader == null) return null;

        ReaderDoc doc = new ReaderDoc(reader.getLogin(), reader.getPassword(), reader.getEmail(), reader.getAge());
        doc.setId(reader.getId());
        doc.setActive(reader.isActive());
        doc.setCurrentLoansCount(reader.getCurrentLoansCount());

        return doc;
    }
}