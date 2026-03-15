package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.library.domain.model.Reader;

@Component
public class ReaderMapper {

    public Reader toDomain(ReaderDoc doc) {
        if (doc == null) return null;

        Reader reader = new Reader(doc.getLogin(), doc.getPassword(), doc.getAge());
        reader.setId(doc.getId());

        return reader;
    }

    public ReaderDoc toDocument(Reader reader) {
        if (reader == null) return null;

        ReaderDoc doc = new ReaderDoc(reader.getLogin(), reader.getPassword(), reader.getAge());
        doc.setId(reader.getId());
        doc.setCurrentLoansCount(reader.getCurrentLoansCount());

        return doc;
    }
}
