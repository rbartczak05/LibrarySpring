package pl.lodz.p.library.adapters.mongo.documents;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("librarian")
public class LibrarianDoc extends UserDoc {
    public LibrarianDoc(String login, String password, String email, String firstName, String lastName, int age, boolean active) {
        super(login, password, email, firstName, lastName, age, active, "LIBRARIAN");
    }

    public LibrarianDoc() {
        super();
    }
}