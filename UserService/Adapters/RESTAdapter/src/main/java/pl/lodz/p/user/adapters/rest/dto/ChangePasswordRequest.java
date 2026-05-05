package pl.lodz.p.user.adapters.rest.dto;

public record ChangePasswordRequest(String oldPassword, String newPassword) {
}