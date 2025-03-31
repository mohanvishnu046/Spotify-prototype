package com.api.spotify_gateway.filter;

import com.api.spotify_gateway.config.JwtUtil;
import com.api.spotify_gateway.exception.InvalidUserException;
import com.api.spotify_gateway.exception.MissingHeaderException;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.config> {

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(config.class);
    }

    @Override
    public GatewayFilter apply(config config) {
        return ((exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw new MissingHeaderException("Missing Authorization Header");
            }
            String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                authHeader=authHeader.substring(7);
            }
            try{
                jwtUtil.validateToken(authHeader);
            }catch (Exception e){
                throw new InvalidUserException("Invalid User",e);
            }
            return chain.filter(exchange);
        });
    }

    public static class config{

    }
}
