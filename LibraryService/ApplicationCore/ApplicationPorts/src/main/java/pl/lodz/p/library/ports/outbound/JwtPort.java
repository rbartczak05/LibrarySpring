package pl.lodz.p.library.ports.outbound;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtPort {
    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean verifySignature(String id, String token);
}