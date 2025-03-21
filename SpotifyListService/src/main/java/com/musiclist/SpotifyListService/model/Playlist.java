package com.musiclist.SpotifyListService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "playlists")
public class Playlist {
    @Id
    private String id;
    private String name;
    private String userId;
    private List<String> trackIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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