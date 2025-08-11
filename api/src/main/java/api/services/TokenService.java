package api.services;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import api.dtos.AuthenticationDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * TokenService.
 */
@Service
public class TokenService {
    public static final long EXPIRATION_TIME_MS = 86400000;

    @SuppressWarnings("checkstyle:LineLength")
    @Value("${jwt.token.secret:XrLHWXPiznJfz3jvJF9ZJkIzvgC0RAF64dOO8bqSxJ2LStAOUAIO85gWg7tcFlfLL9c6q40UCRKMlwnyM5OQOg==}")
    private String secret;

    /**
     * Generate new JWT token.
     *
     * @param authentication User authentication
     * @return {@link AuthenticationDto}
     */
    public AuthenticationDto generateToken(Authentication authentication) {
        String username = authentication.getName();
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expireDate = new Date(now + EXPIRATION_TIME_MS);

        String token = Jwts.builder()
            .subject(username)
            .issuedAt(issuedAt)
            .expiration(expireDate)
            .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
            .compact();

        return new AuthenticationDto(token, "Bearer", expireDate);
    }

    /**
     * Get username from JWT token.
     *
     * @param token JWT token
     * @return Username
     */
    public String getUsernameByToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Token is invalid");
        }
    }
}
