package pl.lodz.p.library.adapters.mongo.documents;

public class AdministratorDoc extends UserDoc {
    public AdministratorDoc(String login, String password, String email, int age) {
        super(login, password, email, age);
    }

    public AdministratorDoc(String login, String email, int age) {
        super(login, email, age);
        setActive(true);
    }

    public AdministratorDoc() {
        super();
    }

    @Override
    public String toString() {
        return "Administrator{} " + super.toString();
    }
}