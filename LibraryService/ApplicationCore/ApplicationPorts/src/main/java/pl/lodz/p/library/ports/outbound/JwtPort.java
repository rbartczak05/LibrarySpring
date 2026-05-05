package pl.lodz.p.library.ports.outbound;

public interface JwtPort {
    String extractUsername(String token);

    boolean isTokenValid(String token);

    boolean verifySignature(String id, String token);
}