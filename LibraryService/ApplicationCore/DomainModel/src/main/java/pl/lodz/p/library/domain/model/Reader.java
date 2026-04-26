package pl.lodz.p.library.domain.model;

import jakarta.validation.constraints.*;
import pl.lodz.p.library.domain.exceptions.UserException;

public abstract class Reader {
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

    public static final int maxLoans = 5;

    @NotNull(message = "Liczba wypożyczeń nie może być wartością null.")
    @Min(value = 0, message = "Czytelnik nie może mieć ujemnej liczby wypożyczeń.")
    @Max(value = maxLoans, message = "Przekroczono maksymalny limit wypożyczeń dla czytelnika (" + maxLoans + ").")
    private int currentLoansCount = 0;

    public Reader(String login, String password, String email, int age) {
        this(login, email, age);
        this.password = password;
    }

    public Reader(String login, String email, int age) {
        this.login = login;
        this.email = email;
        this.age = age;
        this.active = false;
    }

    public Reader() {

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

    public int getMaxLoans() {
        return maxLoans;
    }

    public int getCurrentLoansCount() {
        return currentLoansCount;
    }

    public void setCurrentLoansCount(int currentLoansCount) {
        if (currentLoansCount > maxLoans || currentLoansCount < 0) {
            throw new UserException("Reader with id: " + getId() + " cannot have more than " + maxLoans + " or less than 0 loans.");
        }
        this.currentLoansCount = currentLoansCount;
    }

    public boolean canBorrowBook() {
        if (!isActive()) {
            throw new UserException("Reader with id: " + getId() + " is not active");
        }
        if (currentLoansCount >= maxLoans) {
            throw new UserException("Reader with id: " + getId() + " reached the maximum number of loans.");
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", active=" + active +
                '}';
    }
}
