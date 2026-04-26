package pl.lodz.p.library.domain.model;

public class Librarian extends User {
    public Librarian(String login, String password, String email, String firstName, String lastName, int age) {
        super(login, password, email, firstName, lastName, age);
    }

    public Librarian(String login, String email, String firstName, String lastName, int age) {
        super(login, email, firstName, lastName, age);
    }

    public Librarian() {
        super();
    }

    @Override
    public String toString() {
        return "Librarian{} " + super.toString();
    }
}