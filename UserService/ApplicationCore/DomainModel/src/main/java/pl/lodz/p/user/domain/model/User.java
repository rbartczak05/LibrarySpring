package pl.lodz.p.user.domain.model;

public abstract class User {
    private String id;

    private String login;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private int age;

    private boolean active;

    public User(String login, String password, String email, String firstName, String lastName, int age) {
        this(login, email, firstName, lastName, age);
        this.password = password;
    }

    public User(String login, String email, String firstName, String lastName, int age) {
        this.login = login;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return "User" + "{id=" + id + ", login='" + login + "', email='" + email + "', firstName='" + firstName + "', lastName='" + lastName + "', active=" + active + '}';
    }
}