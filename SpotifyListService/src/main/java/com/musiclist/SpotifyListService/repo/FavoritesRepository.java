package com.musiclist.SpotifyListService.repo;

import com.musiclist.SpotifyListService.model.Favorites;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FavoritesRepository extends MongoRepository<Favorites, String> {
    Favorites findByUserId(String userId);
}