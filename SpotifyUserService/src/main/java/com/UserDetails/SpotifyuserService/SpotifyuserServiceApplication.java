package com.UserDetails.SpotifyuserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
//@EnableDiscoveryClient
public class SpotifyuserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotifyuserServiceApplication.class, args);
	}

//out using any where created to know about bean annotation
//	to find it in actuator/beans
	@Bean
	public UserService userDetailService(){
		return new UserService();
	}


}
