package com.musiclist.SpotifyListService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "favorites")
public class Favorites {
    @Id
    private String userId;
    private List<String> trackIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<String> trackIds) {
        this.trackIds = trackIds;
    }
}