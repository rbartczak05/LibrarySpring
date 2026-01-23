package pl.lodz.p.library.dto;

public class AuthResponse {
    private String token;
    private String role;
    private String login;

    public AuthResponse() {}

    public AuthResponse(String token, String role, String login) {
        this.token = token;
        this.role = role;
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }
}