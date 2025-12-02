package pl.lodz.p.library.web.model;

public class Librarian extends User {
    public Librarian(String login, String email, int age) {
        super(login, email, age);
        setActive(true);
    }

    public Librarian() {
        super();
    }

    @Override
    public String toString() {
        return "Librarian{} " + super.toString();
    }
}
