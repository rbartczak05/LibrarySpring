package pl.lodz.p.library.domain.model;

import jakarta.validation.constraints.*;
import pl.lodz.p.library.domain.exceptions.UserException;

public class Client {
    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String login;

    @NotBlank
    @Size(min = 5)
    private String password;

    @NotBlank
    @Email
    @Size(min = 3, max = 100)
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Min(1)
    private int age;

    private boolean active;

    public static final int maxLoans = 5;

    @NotNull
    @Min(value = 0)
    @Max(value = maxLoans)
    private int currentLoansCount = 0;

    public Client(String login, String password, String email, int age) {
        this(login, email, age);
        this.password = password;
    }

    public Client(String login, String email, int age) {
        this.login = login;
        this.email = email;
        this.age = age;
        this.active = false;
    }

    public Client() {

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
