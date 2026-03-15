package pl.lodz.p.library.adapters.mongo.documents;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public abstract class UserDoc {

    @Id
    private String id;

    @NotBlank(message = "Login nie może być pusty.")
    @Length(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków.")
    @Indexed(unique = true)
    private String login;

    @NotBlank
    @Length(min = 5, message = "Hasło musi mieć co najmniej 5 znaków.")
    private String password;

    @NotBlank(message = "Email nie może być pusty.")
    @Email(message = "Podany ciąg nie jest poprawnym adresem email.")
    @Length(min = 3, max = 100, message = "Email musi mieć od 3 do 100 znaków.")
    @Indexed(unique = true)
    private String email;

    @Min(value = 1, message = "Nie wolno rejestrować nienarodzonych!")
    private int age;
    private boolean active;

    public UserDoc(String login, String password, String email, int age) {
        this(login, email, age);
        this.password = password;
    }

    public UserDoc(String login, String email, int age) {
        this.login = login;
        this.email = email;
        this.age = age;
        this.active = false;
    }

    public UserDoc() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", active=" + active +
                '}';
    }
}
