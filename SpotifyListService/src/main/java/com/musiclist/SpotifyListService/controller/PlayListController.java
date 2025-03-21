package com.musiclist.SpotifyListService.controller;

import com.musiclist.SpotifyListService.model.PlaylistResponse;
import com.musiclist.SpotifyListService.model.Track;
import com.musiclist.SpotifyListService.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/list")
public class PlayListController {
    @Autowired
    private PlayListService playlistService;

    @PostMapping("/playlists")
    public void createPlaylist(@RequestParam String playlistId, @RequestParam String playlistName, @RequestParam String userId) {
        playlistService.createPlaylist(playlistId, playlistName, userId);
    }

    @PostMapping("/playlists/{playlistId}/tracks")
    public void addToPlaylist(@PathVariable String playlistId, @RequestBody Track track) {
        playlistService.addToPlaylist(playlistId, track);
    }

    @DeleteMapping("/playlists/{playlistId}/tracks/{trackId}")
    public void removeFromPlaylist(@PathVariable String playlistId, @PathVariable String trackId) {
        playlistService.removeFromPlaylist(playlistId, trackId);
    }

    @GetMapping("/playlists")
    public List<PlaylistResponse> getPlaylists(@RequestParam String userId) {
        return playlistService.getPlaylists(userId);
    }
}
