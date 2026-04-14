package pl.lodz.p.library.adapters.soap.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "userDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO {
    @XmlElement(name = "id", required = true)
    private String id;

    @NotBlank(message = "Login nie może być pusty.")
    @Size(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków.")
    @XmlElement(name = "login", required = true)
    private String login;

    @NotBlank(message = "Email nie może być pusty.")
    @Email(message = "Podany ciąg nie jest poprawnym adresem email.")
    @Size(min = 3, max = 100, message = "Email musi mieć od 3 do 100 znaków.")
    @XmlElement(name = "email", required = true)
    private String email;

    @Min(value = 1, message = "Nie wolno rejestrować nienarodzonych!")
    @XmlElement(name = "age", required = true)
    private int age;
    @XmlElement(name = "active", required = true)
    private boolean active;
    @XmlElement(name = "type", required = true)
    private String type;
    @XmlElement(name = "currentLoansCount", required = false) //tylko da readera
    private int currentLoansCount;

    public UserDTO() {
    }

    public UserDTO(String id, String login, String email, int age, boolean active, String type, int currentLoansCount) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.age = age;
        this.active = active;
        this.type = type;
        this.currentLoansCount = currentLoansCount;
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

    public int getCurrentLoansCount() {
        return currentLoansCount;
    }

    public void setCurrentLoansCount(int currentLoansCount) {
        this.currentLoansCount = currentLoansCount;
    }
}
