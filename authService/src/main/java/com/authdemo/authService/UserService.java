package com.authdemo.authService;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
//    @Autowired
//    private UserProxy userProxy;
    private static final String USER_LOGIN_TOPIC = "user-login-topic";
    private static final String USER_RESPONSE_TOPIC = "user-response-topic";
    private final Map<String, CompletableFuture<User>> userValidationMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendLoginRequest(String email) {
        kafkaTemplate.send(USER_LOGIN_TOPIC, email);
        log.info("email has been published {} topic {}",email,USER_LOGIN_TOPIC);
        CompletableFuture<User> future = new CompletableFuture<>();
        userValidationMap.put(email, future);
    }

    public User validateUser(User user) {
        try {
            // Wait for the Kafka response for the given email
            CompletableFuture<User> future = userValidationMap.get(user.getEmail());
            if (future == null) {
                return null; // No future associated with the email
            }

            // Wait for the response with a timeout (e.g., 5 seconds)
            User kafkaUser = future.get(5, TimeUnit.SECONDS);

            // Compare the received user data with the provided credentials
            if (kafkaUser != null &&
                    kafkaUser.getEmail().equals(user.getEmail()) &&
                    kafkaUser.getPassword().equals(user.getPassword())) {
                return kafkaUser; // Return the user from the Kafka topic
            } else {
                return null; // Validation failed
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
//            return fetchUserFeign(user.getEmail());
        }
    }


    @KafkaListener(topics = USER_RESPONSE_TOPIC, groupId = "group_01")
    public void consumeUserResponse(String user) {
        log.info("User is consumed {} topic {}",user,USER_RESPONSE_TOPIC);
        try {
            JsonNode jsonResponse = objectMapper.readTree(user);

            String email = jsonResponse.get("email").asText();
            String password = jsonResponse.get("password").asText();
            String userId = jsonResponse.get("userId").asText();

            User kafkaUser = new User(email, password, userId);

            CompletableFuture<User> future = userValidationMap.get(email);
            if (future != null) {
                future.complete(kafkaUser);
                userValidationMap.remove(email);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public User fetchUserFeign(String email) {
//        User user = userProxy.fetchDetails(email);
//        log.info("User Fetched by feign :: {}",user.toString());
//        if(user!=null)
//            return user;
//        else return new User();
//    }

}
