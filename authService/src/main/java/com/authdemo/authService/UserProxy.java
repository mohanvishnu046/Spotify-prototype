package com.authdemo.authService;


//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "SpotifyuserService", url="localhost:8010")
public interface UserProxy {

    @GetMapping("/api/v1/user")
    User fetchDetails(@RequestParam(name = "email") String email);
}