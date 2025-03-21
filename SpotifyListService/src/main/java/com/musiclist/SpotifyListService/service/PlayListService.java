package com.musiclist.SpotifyListService.service;


import com.musiclist.SpotifyListService.model.Favorites;
import com.musiclist.SpotifyListService.model.Playlist;
import com.musiclist.SpotifyListService.model.PlaylistResponse;
import com.musiclist.SpotifyListService.model.Track;
import com.musiclist.SpotifyListService.repo.FavoritesRepository;
import com.musiclist.SpotifyListService.repo.PlaylistRepository;
import com.musiclist.SpotifyListService.repo.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PlayListService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private TrackRepository trackRepository;

    public void createPlaylist(String playlistId, String playlistName, String userId) {
        Playlist playlist = new Playlist();
        playlist.setId(playlistId);
        playlist.setName(playlistName);
        playlist.setUserId(userId);
        playlist.setTrackIds(new ArrayList<>());
        playlistRepository.save(playlist);
    }

    public void addToPlaylist(String playlistId, Track track) {
        Optional<Track> existingTrack = trackRepository.findById(track.getId());
        if (existingTrack.isEmpty()) {
            trackRepository.save(track);
        }
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        playlist.ifPresent(p -> {
            p.getTrackIds().add(track.getId());
            playlistRepository.save(p);
        });
    }

    public void removeFromPlaylist(String playlistId, String trackId) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        playlist.ifPresent(p -> {
            p.getTrackIds().remove(trackId);
            playlistRepository.save(p);
        });
    }

    public List<PlaylistResponse> getPlaylists(String userId) {
        List<Playlist> playlists = playlistRepository.findByUserId(userId);
        List<PlaylistResponse> playlistResponses = new ArrayList<>();

        for (Playlist playlist : playlists) {
            List<Track> tracks = trackRepository.findByIdIn(playlist.getTrackIds());
            PlaylistResponse response = new PlaylistResponse(
                    playlist.getId(),
                    playlist.getName(),
                    tracks
            );
            playlistResponses.add(response);
        }

        return playlistResponses;
    }
}