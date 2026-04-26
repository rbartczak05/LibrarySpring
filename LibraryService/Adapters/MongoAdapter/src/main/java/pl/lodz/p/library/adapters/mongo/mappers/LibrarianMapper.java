package pl.lodz.p.library.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.library.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.library.domain.model.Librarian;

@Component
public class LibrarianMapper {

    public Librarian toDomain(LibrarianDoc doc) {
        if (doc == null) return null;
        Librarian lib = new Librarian(doc.getLogin(), doc.getPassword(), doc.getEmail(), doc.getAge());
        lib.setId(doc.getId());
        lib.setActive(doc.isActive());
        return lib;
    }

    public LibrarianDoc toDocument(Librarian lib) {
        if (lib == null) return null;
        LibrarianDoc doc = new LibrarianDoc(lib.getLogin(), lib.getPassword(), lib.getEmail(), lib.getAge());
        doc.setId(lib.getId());
        doc.setActive(lib.isActive());
        return doc;
    }
}