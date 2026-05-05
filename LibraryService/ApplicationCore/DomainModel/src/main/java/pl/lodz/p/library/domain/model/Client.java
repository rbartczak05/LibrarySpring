package pl.lodz.p.library.domain.model;

import pl.lodz.p.library.domain.exceptions.ClientException;

public class Client {
    public static final int maxLoans = 5;

    private int currentLoansCount = 0;

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private int age;

    private boolean active;

    public Client(String firstName, String lastName, String email, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ClientException("First name cannot be blank");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ClientException("Last name cannot be blank");
        }
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || email.length() < 3 || email.length() > 100) {
            throw new ClientException("Invalid email length");
        }
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 1) {
            throw new ClientException("Age must be at least 1");
        }
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
            throw new ClientException("Client with id: " + getId() + " cannot have more than " + maxLoans + " or less than 0 loans.");
        }
        this.currentLoansCount = currentLoansCount;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public boolean canBorrowBook() {
        if (!isActive()) {
            throw new ClientException("Client with id: " + getId() + " is not active");
        }
        if (currentLoansCount >= maxLoans) {
            throw new ClientException("Client with id: " + getId() + " reached the maximum number of loans.");
        }
        return true;
    }

    @Override
    public String toString() {
        return "Client{" + "id='" + id + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", age=" + age + ", active=" + active + '}';
    }
}