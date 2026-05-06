package pl.lodz.p.user.adapters.soap.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.lodz.p.user.ports.outbound.JwtPort;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtSoapService implements JwtPort {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

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
        return Jwts.builder().subject(String.valueOf(id))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public boolean verifySignature(UUID id, String token) {
        try {
            return extractUsername(token).equals(id);
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
                .verifyWith(getSignInKey())
                .build().parseSignedClaims(token).getPayload();
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder().claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}