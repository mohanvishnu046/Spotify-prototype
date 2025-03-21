package com.musiclist.SpotifyListService.repo;

import com.musiclist.SpotifyListService.model.Track;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrackRepository extends MongoRepository<Track, String> {
    List<Track> findByIdIn(List<String> trackIds);
}