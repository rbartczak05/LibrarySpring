package pl.lodz.p.library.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public abstract class User {
    private String id;

    @NotBlank(message = "Login nie może być pusty.")
    @Size(min = 3, max = 20, message = "Login musi mieć od 3 do 20 znaków.")
    private String login;

    @NotBlank
    @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków.")
    private String password;

    @NotBlank(message = "Email nie może być pusty.")
    @Email(message = "Podany ciąg nie jest poprawnym adresem email.")
    @Size(min = 3, max = 100, message = "Email musi mieć od 3 do 100 znaków.")
    private String email;

    @Min(value = 1, message = "Nie wolno rejestrować nienarodzonych!")
    private int age;
    private boolean active;

    public User(String login, String password, String email, int age) {
        this(login, email, age);
        this.password = password;
    }

    public User(String login, String email, int age) {
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
