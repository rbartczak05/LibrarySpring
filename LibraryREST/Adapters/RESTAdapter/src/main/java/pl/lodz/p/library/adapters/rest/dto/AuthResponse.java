package pl.lodz.p.library.adapters.rest.dto;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String role;
    private String login;

    public AuthResponse() {}

    public AuthResponse(String token, String refreshToken, String role, String login) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }
}