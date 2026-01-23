package pl.lodz.p.library.model;

public class Librarian extends User {
    public Librarian(String login, String password, String email, int age) {
        super(login, password, email, age);
    }

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
