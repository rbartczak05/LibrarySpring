package pl.lodz.p.user.ports.outbound;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface JwtPort {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateSignatureForId(UUID id);

    boolean verifySignature(UUID id, String token);
}
