package pl.lodz.p.library.adapters.soap.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "registerRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterRequest {
    @NotBlank(message = "Login nie może być pusty.")
    @Size(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków.")
    @XmlElement(name = "login", required = true)
    private String login;

    @NotBlank(message = "Hasło nie może być puste.")
    @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków.")
    @XmlElement(name = "password", required = true)
    private String password;

    @NotBlank(message = "Email nie może być pusty.")
    @Email(message = "Podaj poprawny adres email.")
    @XmlElement(name = "email", required = true)
    private String email;

    @Min(value = 1, message = "Wiek musi być liczbą dodatnią.")
    @XmlElement(name = "age", required = true)
    private int age;

    public RegisterRequest() {
    }

    public RegisterRequest(String login, String password, String email, int age) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.age = age;
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
}
