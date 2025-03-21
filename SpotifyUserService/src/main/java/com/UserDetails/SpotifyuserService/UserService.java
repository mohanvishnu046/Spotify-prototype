package com.UserDetails.SpotifyuserService;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepo userRepo;
    private final static String USER_LOGIN_TOPIC="user-login-topic";
    private static final String USER_RESPONSE_TOPIC = "user-response-topic";
    @Autowired
    private Gson gson;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    public User setUserDetails(User user) throws UserExistExcetion {
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());
        if(existingUser.isEmpty()) {
            user.setUserId(UUID.randomUUID().toString());
            return userRepo.save(user);
        }else throw new UserExistExcetion("User with email "+user.getEmail()+" is exist");
    }

    public User fetchUserByEmail(String email){
        Optional<User> existingUser = userRepo.findByEmail(email);
        return existingUser.orElseGet(() -> new User(email, "NOT_FOUND", "NOT_FOUND"));
    }

    @KafkaListener(topics =USER_LOGIN_TOPIC, groupId = "user-group")
    public void listenForLoginRequest(String email) {
        log.info("email consumed {} and topic {} ",email,USER_LOGIN_TOPIC);

        User user = fetchUserByEmail(email);
        log.info("user found with email {} and user data is {} ",email,user.toString());

        kafkaTemplate.send(USER_RESPONSE_TOPIC,gson.toJson(user));
        log.info("user has been published to {}",USER_RESPONSE_TOPIC);
    }
}
