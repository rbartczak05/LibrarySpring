package pl.lodz.p.library.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.UserHasInvalidFieldValueException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Reader.class, name = "reader"),
        @JsonSubTypes.Type(value = Librarian.class, name = "librarian"),
        @JsonSubTypes.Type(value = Administrator.class, name = "admin")
})
@Document(collection = "users")
public abstract class User {

    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String login;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String email;

    @Min(value = 1)
    @NotNull
    private int age;

    @NotNull
    private boolean active;

    public User(String login, String email, int age) {
        if (login == null || login.isEmpty() || email == null || !email.contains("@") || age <= 0) {
            throw new UserHasInvalidFieldValueException(HttpStatus.CONFLICT, "Login, email or age can't be null or empty");
        }
        this.login = login;
        this.email = email;
        this.age = age;
        this.active = false;
    }

    public User() {

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
        if (login != null && !login.isEmpty()) {
            this.login = login;
        } else {
            throw new UserHasInvalidFieldValueException(HttpStatus.CONFLICT, "Login can't be null or empty");
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        } else {
            throw new UserHasInvalidFieldValueException(HttpStatus.CONFLICT, "Email can't be null or empty");
        }

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        } else {
            throw new UserHasInvalidFieldValueException(HttpStatus.CONFLICT, "Age must be greater than 0");
        }

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
