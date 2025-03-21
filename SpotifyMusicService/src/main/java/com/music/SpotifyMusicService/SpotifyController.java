package com.music.SpotifyMusicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SpotifyController {
    private static final Logger log = LoggerFactory.getLogger(SpotifyController.class);
    @Autowired
    private SpotifyService spotifyService;
    @Autowired
    private AuthService authService;

//    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/music/search")
    public ResponseEntity<?> searchMusic(@RequestParam(name = "q") String q) {
        //jwt validation
//         throws JsonProcessingException
//        ,@RequestHeader(name = "Authorization") String authHeader
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
//        }
//        String token = authHeader.replace("Bearer ", "");
//        log.info("token from search music is {}",token);
        try {
//            boolean flag = authService.validateToken(token);
//            if (flag) {
                String accessToken = spotifyService.getAccessToken();
                return ResponseEntity.ok(spotifyService.searchMusic(q, accessToken));
//            }else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");

        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Exception from searchMusic service"+e.getMessage());
        }
    }
}
