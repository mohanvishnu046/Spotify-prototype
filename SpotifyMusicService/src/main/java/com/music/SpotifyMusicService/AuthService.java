package com.music.SpotifyMusicService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static String secret ="jwtToken that is used as secret key for token";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    public boolean validateToken(String token) {
        log.info("Validating token {}",token);
        try {
            // Parse the token and extract claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey()) // Set the secret key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Check if the token is expired
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // Token is invalid
            return false;
        }
    }
}