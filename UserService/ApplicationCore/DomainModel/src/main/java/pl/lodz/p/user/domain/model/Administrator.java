package pl.lodz.p.user.domain.model;

public class Administrator extends User {
    public Administrator(String login, String password, String email, String firstName, String lastName, int age) {
        super(login, password, email, firstName, lastName, age);
    }

    public Administrator(String login, String email, String firstName, String lastName, int age) {
        super(login, email, firstName, lastName, age);
    }

    public Administrator() {
        super();
    }

    @Override
    public String toString() {
        return "Administrator{} " + super.toString();
    }
}