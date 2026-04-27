package pl.lodz.p.user.domain.model;

public class Reader extends User {
    public Reader(String login, String password, String email, String firstName, String lastName, int age) {
        super(login, password, email, firstName, lastName, age);
    }

    public Reader(String login, String email, String firstName, String lastName, int age) {
        super(login, email, firstName, lastName, age);
    }

    public Reader() {
        super();
    }

    @Override
    public String toString() {
        return "Reader{} " + super.toString();
    }
}