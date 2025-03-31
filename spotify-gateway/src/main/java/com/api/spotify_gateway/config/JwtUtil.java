package com.api.spotify_gateway.config;

import com.api.spotify_gateway.exception.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class JwtUtil{

    private static final String secret = "jwtToken that is used as secret key for token";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public void validateToken(final String token){
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        }catch (ExpiredJwtException e){
            throw new TokenExpiredException("Token is expired", e);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
