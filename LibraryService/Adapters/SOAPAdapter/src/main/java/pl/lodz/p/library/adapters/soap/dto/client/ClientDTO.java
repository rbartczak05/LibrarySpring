package pl.lodz.p.library.adapters.soap.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.UUID;

@XmlType(name = "clientDTO")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientDTO {
    @XmlElement(name = "id")
    private UUID id;

    @NotBlank(message = "Imię nie może być puste")
    @XmlElement(name = "firstName", required = true)
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    @XmlElement(name = "lastName", required = true)
    private String lastName;

    @NotBlank
    @Email(message = "Niepoprawny format email")
    @Size(min = 3, max = 100)
    @XmlElement(name = "email", required = true)
    private String email;

    @Min(value = 1, message = "Wiek musi być większy od zera")
    @XmlElement(name = "age", required = true)
    private int age;

    @XmlElement(name = "active", required = true)
    private boolean active;

    @XmlElement(name = "currentLoansCount", required = true)
    private int currentLoansCount;

    public ClientDTO() {
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
}