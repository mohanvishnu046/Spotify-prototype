package com.api.spotify_gateway.config;

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
public class JwtAuthenticationFilter implements GatewayFilter {

    private static final String secret = "jwtToken that is used as secret key for token";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Validate and decode the JWT token
                String subject = Jwts.parser()
                        .setSigningKey(getSecretKey())
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();  // Extract the subject (user ID)

                // Store the subject in the request context (header)
                exchange.getRequest().mutate()
                        .header("subject", subject)
                        .build();
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Invalid JWT token"));
            }
        }
        return chain.filter(exchange);
    }
}
