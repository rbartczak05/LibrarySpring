package pl.lodz.p.user.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.user.adapters.mongo.documents.ReaderDoc;
import pl.lodz.p.user.domain.model.Reader;

@Component
public class ReaderMapper {
    public Reader toDomain(ReaderDoc readerDoc) {
        if (readerDoc == null) {
            return null;
        }
        Reader reader = new Reader(
                readerDoc.getLogin(),
                readerDoc.getPassword(),
                readerDoc.getEmail(),
                readerDoc.getFirstName(),
                readerDoc.getLastName(),
                readerDoc.getAge()
        );
        reader.setId(readerDoc.getId());
        reader.setActive(readerDoc.isActive());
        return reader;
    }

    public ReaderDoc toDocument(Reader reader) {
        if (reader == null) {
            return null;
        }
        ReaderDoc readerDoc = new ReaderDoc(
                reader.getLogin(),
                reader.getPassword(),
                reader.getEmail(),
                reader.getFirstName(),
                reader.getLastName(),
                reader.getAge(),
                reader.isActive()
        );
        readerDoc.setId(reader.getId());
        return readerDoc;
    }
}