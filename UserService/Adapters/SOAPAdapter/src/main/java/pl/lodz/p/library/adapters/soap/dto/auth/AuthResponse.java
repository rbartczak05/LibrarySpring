package pl.lodz.p.library.adapters.soap.dto.auth;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AuthResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthResponse {
    @XmlElement(required = true)
    private String token;
    @XmlElement(required = true)
    private String refreshToken;
    @XmlElement(required = true)
    private String role;
    @XmlElement(required = true)
    private String login;

    public AuthResponse() {
    }

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