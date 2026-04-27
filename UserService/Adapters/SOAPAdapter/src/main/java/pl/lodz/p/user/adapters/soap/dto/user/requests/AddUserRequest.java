package pl.lodz.p.user.adapters.soap.dto.user.requests;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"login", "password", "email", "firstName", "lastName", "age", "accessLevel"})
@XmlRootElement(name = "addUserRequest")
public class AddUserRequest {

    @XmlElement(required = true)
    protected String login;

    @XmlElement(required = true)
    protected String password;

    @XmlElement(required = true)
    protected String email;

    @XmlElement(required = true)
    protected String firstName;

    @XmlElement(required = true)
    protected String lastName;

    protected int age;

    @XmlElement(required = true)
    protected String accessLevel;

    public String getLogin() {
        return login;
    }

    public void setLogin(String value) {
        this.login = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String value) {
        this.firstName = value;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String value) {
        this.lastName = value;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int value) {
        this.age = value;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String value) {
        this.accessLevel = value;
    }
}