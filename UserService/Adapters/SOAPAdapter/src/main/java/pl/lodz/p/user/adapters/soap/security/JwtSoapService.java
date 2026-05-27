package pl.lodz.p.user.adapters.soap.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.lodz.p.user.ports.outbound.JwtPort;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtSoapService implements JwtPort {

    private static final long ACCESS_TOKEN_EXPIRATION  = 1000L * 60 * 15;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 7;

    @Value("${jwt.private.key}")
    private String privateKeyStr;

    @Value("${jwt.public.key}")
    private String publicKeyStr;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }

    @Override
    public String generateSignatureForId(UUID id) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    @Override
    public boolean verifySignature(UUID id, String token) {
        try {
            return extractUsername(token).equals(String.valueOf(id));
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getPublicKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getPrivateKey(), Jwts.SIG.RS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(privateKeyStr);
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Nie można wczytać klucza prywatnego RSA", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(publicKeyStr);
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Nie można wczytać klucza publicznego RSA", e);
        }
    }
}