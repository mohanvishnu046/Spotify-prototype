package com.musiclist.SpotifyListService.repo;

import com.musiclist.SpotifyListService.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    List<Playlist> findByUserId(String userId);
}