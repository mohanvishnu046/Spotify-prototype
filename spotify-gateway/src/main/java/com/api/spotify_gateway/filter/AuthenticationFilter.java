package com.api.spotify_gateway.filter;

import com.api.spotify_gateway.config.JwtUtil;
import com.api.spotify_gateway.exception.InvalidTokenException;
import com.api.spotify_gateway.exception.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;
    public AuthenticationFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return handleError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                jwtUtil.validateToken(token);
                return chain.filter(exchange);
            } catch (TokenExpiredException | InvalidTokenException ex) {
                return handleError(exchange, ex.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> handleError(ServerWebExchange exchange, String error, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", status.getReasonPhrase(), error);
        return response.writeWith(Mono.just(response.bufferFactory()
                .wrap(jsonResponse.getBytes(StandardCharsets.UTF_8))));
    }

    public static class Config {
        // Configuration properties if needed
    }
}