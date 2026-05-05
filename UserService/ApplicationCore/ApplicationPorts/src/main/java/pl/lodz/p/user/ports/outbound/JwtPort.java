package pl.lodz.p.user.ports.outbound;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtPort {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateSignatureForId(String id);

    boolean verifySignature(String id, String token);
}
