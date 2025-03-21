package com.UserDetails.SpotifyuserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        try{
            return new ResponseEntity<>(userService.setUserDetails(user), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/searchUser")
    public ResponseEntity<?> fetchUser(@RequestParam(name = "email") String email){
        try{
            return new ResponseEntity<>(userService.fetchUserByEmail(email), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("User Not exist", HttpStatus.NOT_FOUND);
        }
    }
}
