package pl.lodz.p.library.adapters.mongo.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "clients")
public class ClientDoc {
    public static final int maxLoans = 5;

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private int age;

    private boolean active;

    private int currentLoansCount = 0;

    public ClientDoc(String firstName, String lastName, String email, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.active = false;
    }

    public ClientDoc() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int getCurrentLoansCount() {
        return currentLoansCount;
    }

    public void setCurrentLoansCount(int currentLoansCount) {
        this.currentLoansCount = currentLoansCount;
    }

    public int getMaxLoans() {
        return maxLoans;
    }
}