package com.musiclist.SpotifyListService.service;

import com.musiclist.SpotifyListService.model.Favorites;
import com.musiclist.SpotifyListService.model.Track;
import com.musiclist.SpotifyListService.repo.FavoritesRepository;
import com.musiclist.SpotifyListService.repo.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;

    public Track addToFavorites(String userId, Track track) {
        Optional<Track> existingTrack = trackRepository.findById(track.getId());
        if (existingTrack.isEmpty()) {
            trackRepository.save(track);
        }

        Favorites favorites = favoritesRepository.findByUserId(userId);
        if (favorites == null) {
            favorites = new Favorites();
            favorites.setUserId(userId);
            favorites.setTrackIds(new ArrayList<>());
        }
        if (!favorites.getTrackIds().contains(track.getId())) {
            favorites.getTrackIds().add(track.getId());
            favoritesRepository.save(favorites);
        }
        return track;
    }

    public boolean removeFromFavorites(String userId, String trackId) {
        Favorites favorites = favoritesRepository.findByUserId(userId);
        if (favorites != null) {
            favorites.getTrackIds().remove(trackId);
            favoritesRepository.save(favorites);
            return true;
        }
        return false;
    }

    public List<Track> getFavorites(String userId) {
        Favorites favorites = favoritesRepository.findByUserId(userId);
        if (favorites == null || favorites.getTrackIds().isEmpty()) {
            return List.of();
        }
        return trackRepository.findByIdIn(favorites.getTrackIds());
    }
}
