package pl.lodz.p.user.adapters.rest.dto;

public record AuthResponse(String token, String refreshToken, String role, String login) {
}