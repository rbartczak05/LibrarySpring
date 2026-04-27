package pl.lodz.p.user.adapters.mongo.documents;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@TypeAlias("administrator")
public class AdministratorDoc extends UserDoc {
    public AdministratorDoc(String login, String password, String email, String firstName, String lastName, int age, boolean active) {
        super(login, password, email, firstName, lastName, age, active, "ADMINISTRATOR");
    }

    public AdministratorDoc() {
        super();
    }
}