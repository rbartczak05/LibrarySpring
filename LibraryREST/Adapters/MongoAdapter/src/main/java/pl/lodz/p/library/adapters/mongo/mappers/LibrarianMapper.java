package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.library.domain.model.Librarian;

@Component
public class LibrarianMapper {
    public Librarian toDomain(LibrarianDoc doc) {
        if (doc == null) return null;

        Librarian librarian = new Librarian(doc.getLogin(), doc.getPassword(), doc.getEmail(), doc.getAge());
        librarian.setId(doc.getId());
        librarian.setActive(doc.isActive());

        return librarian;
    }

    public LibrarianDoc toDocument(Librarian librarian) {
        if (librarian == null) return null;

        LibrarianDoc doc = new LibrarianDoc(librarian.getLogin(), librarian.getPassword(), librarian.getEmail(), librarian.getAge());
        doc.setId(librarian.getId());
        doc.setActive(librarian.isActive());

        return doc;
    }
}
