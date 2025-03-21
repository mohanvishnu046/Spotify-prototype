package com.authdemo.authService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService  userService;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60*48;
    private static String secret ="jwtToken that is used as secret key for token";
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){
        userService.sendLoginRequest(user.getEmail());
        User kafkaUser = userService.validateUser(user);
        if(kafkaUser!=null){
            String token =generateToken(kafkaUser);
            Map<String,String> map=new HashMap<>();
            map.put("token", token);
            map.put("message", "User Successfully logged In");
            return ResponseEntity.ok(map);
        }else return new ResponseEntity<>("Invalid User", HttpStatus.UNAUTHORIZED);
    }
    private String generateToken(User user){
        log.info("the UserId for user {} is {}",user.getEmail(),user.getUserId());
        return Jwts.builder()
                .setSubject(user.getUserId())
                .setAudience("Spotify Users")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .compact();
    }

}
