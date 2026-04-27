package pl.lodz.p.user.adapters.mongo.mappers;

import org.springframework.stereotype.Component;
import pl.lodz.p.user.adapters.mongo.documents.LibrarianDoc;
import pl.lodz.p.user.domain.model.Librarian;

@Component
public class LibrarianMapper {
    public Librarian toDomain(LibrarianDoc librarianDoc) {
        if (librarianDoc == null) {
            return null;
        }
        Librarian librarian = new Librarian(
                librarianDoc.getLogin(),
                librarianDoc.getPassword(),
                librarianDoc.getEmail(),
                librarianDoc.getFirstName(),
                librarianDoc.getLastName(),
                librarianDoc.getAge()
        );
        librarian.setId(librarianDoc.getId());
        librarian.setActive(librarianDoc.isActive());
        return librarian;
    }

    public LibrarianDoc toDocument(Librarian librarian) {
        if (librarian == null) {
            return null;
        }
        LibrarianDoc librarianDoc = new LibrarianDoc(
                librarian.getLogin(),
                librarian.getPassword(),
                librarian.getEmail(),
                librarian.getFirstName(),
                librarian.getLastName(),
                librarian.getAge(),
                librarian.isActive()
        );
        librarianDoc.setId(librarian.getId());
        return librarianDoc;
    }
}