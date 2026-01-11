package pl.lodz.p.library.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.http.HttpStatus;
import pl.lodz.p.library.exception.UserHasInvalidFieldValueException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReaderDTO.class, name = "reader"),
        @JsonSubTypes.Type(value = LibrarianDTO.class, name = "librarian"),
        @JsonSubTypes.Type(value = AdministratorDTO.class, name = "admin")
})
public abstract class UserDTO {
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

    private String type;

    public UserDTO() {
    }

    public UserDTO(String id, String login, String email, int age, boolean active, String type) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.age = age;
        this.active = active;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
