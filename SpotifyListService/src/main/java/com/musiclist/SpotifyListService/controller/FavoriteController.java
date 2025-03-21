package com.musiclist.SpotifyListService.controller;

import com.musiclist.SpotifyListService.model.Track;
import com.musiclist.SpotifyListService.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/list")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/favorites")
    public ResponseEntity<?> addToFavorites(@RequestParam String userId, @RequestBody Track track) {
        return new ResponseEntity<>(favoriteService.addToFavorites(userId, track), HttpStatus.CREATED);
    }

    @DeleteMapping("/favorites/{userId}/{trackId}")
    public ResponseEntity<?> removeFromFavorites(@PathVariable String userId, @PathVariable String trackId) {
        boolean flag = favoriteService.removeFromFavorites(userId, trackId);
        if(flag) return ResponseEntity.ok("Track is removed");
        else return new ResponseEntity<>("track is not in the list", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/favorites/{userId}")
    public  ResponseEntity<?> getFavorites(@PathVariable String userId) {
        return ResponseEntity.ok(favoriteService.getFavorites(userId));
    }
}
