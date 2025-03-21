package com.api.spotify_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpotifyGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotifyGatewayApplication.class, args);
	}

}
