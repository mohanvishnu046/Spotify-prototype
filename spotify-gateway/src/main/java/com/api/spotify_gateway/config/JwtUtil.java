package com.api.spotify_gateway.config;

import com.api.spotify_gateway.exception.TokenExpiredException;
import com.api.spotify_gateway.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET = "jwtToken that is used as secret key for token";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public void validateToken(String token) throws InvalidTokenException, TokenExpiredException {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("Token expired");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw new InvalidTokenException("Invalid token");
        } catch (IllegalArgumentException ex) {
            throw new InvalidTokenException("Token cannot be empty");
        }
    }
}