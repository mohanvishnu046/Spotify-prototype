import React, { useState, useEffect } from "react";
import { FaSearch } from "react-icons/fa";

export const Playlist = ({
  existingPlaylists = [], 
  removeFromPlaylist,
}) => {
  const [searchQuery, setSearchQuery] = useState("");
  const [filteredPlaylists, setFilteredPlaylists] = useState(existingPlaylists);

  useEffect(() => {
    if (!searchQuery) {
      setFilteredPlaylists(existingPlaylists);
    } else {
      const filtered = existingPlaylists.filter((playlist) =>
        playlist.name.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setFilteredPlaylists(filtered);
    }
  }, [searchQuery, existingPlaylists]);

  return (
    <div>
      <h2 className="text-dark mt-5">Playlists</h2>
      {/* Search Playlists */}
      <div className="input-group mb-3">
        <input
          type="text"
          className="form-control"
          placeholder="Search playlists"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <button className="btn btn-info mt-2" onClick={() => {}}>
          <FaSearch /> Search
        </button>
      </div>

      {/* Display Playlists */}
      <div>
        {filteredPlaylists.length === 0 ? (
          <p>No playlists found.</p>
        ) : (
          <ul className="list-group">
            {filteredPlaylists.map((playlist) => (
              <li key={playlist.id} className="list-group-item">
                <h5>{playlist.name}</h5>
                <ul>
                  {/* Check if songs are defined and have any length */}
                  {playlist.tracks && playlist.tracks.length > 0 ? (
                    playlist.tracks.map((tracks) => (
                      <li key={tracks.id} className="list-group-item">
                        {!tracks.album.images.length ? (
                    <span className="track-name-text">{tracks.name.split(" ")[0]}</span>
                  ) : (
                    <img
                      src={tracks.album.images.length > 0 ? tracks.album.images[0].url : ""}
                      alt={tracks.name}
                      className="track-image"
                    />
                  )}
                  <div className="track-info">
                    {tracks.name} - {tracks.artists[0].name}
                  </div>
                        <button
                          className="btn btn-danger btn-sm ml-2"
                          onClick={() => removeFromPlaylist(playlist.id, tracks.id)}
                        >
                          Remove
                        </button>
                      </li>
                    ))
                  ) : (
                    <p>No songs in this playlist.</p>
                  )}
                </ul>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};
