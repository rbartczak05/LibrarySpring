package pl.lodz.p.library.adapters.rest.dto;

import jakarta.validation.constraints.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "clients", itemRelation = "client")
public class ClientDTO extends RepresentationModel<ClientDTO> {
    private String id;

    @NotBlank(message = "Imię nie może być puste")
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    private String lastName;

    @NotBlank
    @Email(message = "Niepoprawny format email")
    @Size(min = 3, max = 100)
    private String email;

    @Min(value = 1, message = "Wiek musi być większy od zera")
    private int age;

    private boolean active;
    private int currentLoansCount;

    public ClientDTO() {
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
}