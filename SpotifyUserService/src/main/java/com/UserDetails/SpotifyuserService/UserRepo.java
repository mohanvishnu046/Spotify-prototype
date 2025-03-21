package com.UserDetails.SpotifyuserService;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends MongoRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
