package com.api.spotify_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilterFactory implements GatewayFilterFactory<JwtAuthenticationFilterFactory.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        // We return a new instance of JwtAuthenticationFilter. If you want to use the config, you can modify the filter behavior.
        return new JwtAuthenticationFilter();
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    // This can hold any configuration for the filter if you need it (like JWT specific configs).
    public static class Config {
        // You can add configuration properties like "secretKey" or "issuer", if needed.
    }
}
