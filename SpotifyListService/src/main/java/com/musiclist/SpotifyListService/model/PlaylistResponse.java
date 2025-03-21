package com.musiclist.SpotifyListService.model;

import java.util.List;

public class PlaylistResponse {
    private String id;
    private String name;
    private List<Track> tracks;

    public PlaylistResponse(String id, String name, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.tracks = tracks;
    }

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

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}