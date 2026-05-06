package pl.lodz.p.library.ports.outbound;

import java.util.UUID;

public interface JwtPort {
    String extractUsername(String token);

    boolean isTokenValid(String token);

    boolean verifySignature(UUID id, String token);
}