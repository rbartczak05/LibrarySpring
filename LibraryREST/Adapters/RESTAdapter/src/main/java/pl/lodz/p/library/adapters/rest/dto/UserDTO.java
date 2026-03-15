package pl.lodz.p.library.adapters.rest.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReaderDTO.class, name = "reader"),
        @JsonSubTypes.Type(value = LibrarianDTO.class, name = "librarian"),
        @JsonSubTypes.Type(value = AdministratorDTO.class, name = "admin")
})
public abstract class UserDTO {
    @Id
    private String id;

    @NotBlank(message = "Login nie może być pusty.")
    @Length(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków.")
    private String login;

    @NotBlank(message = "Email nie może być pusty.")
    @Email(message = "Podany ciąg nie jest poprawnym adresem email.")
    @Length(min = 3, max = 100, message = "Email musi mieć od 3 do 100 znaków.")
    private String email;

    @Min(value = 1, message = "Nie wolno rejestrować nienarodzonych!")
    private int age;
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
        this.login = login;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
