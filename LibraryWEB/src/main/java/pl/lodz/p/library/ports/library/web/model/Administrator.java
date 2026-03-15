package pl.lodz.p.library.ports.library.web.model;

public class Administrator extends User {
    public Administrator(String login, String email, int age) {
        super(login, email, age);
        setActive(true);
    }

    public Administrator() {
        super();
    }

    @Override
    public String toString() {
        return "Administrator{} " + super.toString();
    }
}