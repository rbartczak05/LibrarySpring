package pl.lodz.p.library.adapters.mongo.documents;

public class LibrarianDoc extends UserDoc {
    public LibrarianDoc(String login, String password, String email, int age) {
        super(login, password, email, age);
    }

    public LibrarianDoc(String login, String email, int age) {
        super(login, email, age);
        setActive(true);
    }

    public LibrarianDoc() {
        super();
    }

    @Override
    public String toString() {
        return "Librarian{} " + super.toString();
    }
}
