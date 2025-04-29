import React, { useState, useEffect } from "react";
import { FaSearch, FaHeart,FaHeartBroken,FaPlus,FaPlay} from "react-icons/fa";
import AddToPlaylistModal from "../components/AddToPlaylistModal";
import Player from "../components/Player";

export const Search = ({ token, addToFavorites,removeFromFavorites,favorites,playlists, addToPlaylist }) => {
  const [searchTerm, setSearchTerm] = useState("telugu");
  const [showModal, setShowModal] = useState(false);
  const [selectedTrack, setSelectedTrack] = useState(null);
  const [tracks, setTracks] = useState([]); 
  const [currentTrack, setCurrentTrack] = useState(null);
  const authToken = localStorage.getItem("authToken") || null;
  const isFavorite = (track) => {
    return favorites.some((favoriteTrack) => favoriteTrack.id === track.id);
  };

  const handlePlay = (track) => {
    setCurrentTrack(track); // Set the track to be played
  };

  const searchTracks = async () => {
    if (!searchTerm || !token) return;

    try {
      const response = await fetch(
        `https://api.spotify.com/v1/search?q=${searchTerm}&type=track`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (!response.ok) {
        throw new Error("Failed to fetch tracks");
      }

      const data = await response.json();

      if (data.tracks.items.length === 0) {
        setTracks([]);
      } else {
        setTracks(data.tracks.items);
      }
    } catch (error) {
      console.error("Error fetching tracks:", error);
    }
  };

  useEffect(() => {
    if (token) {
      searchTracks();
    }
  }, [token]);

  const handleAddToPlaylist = (track) => {
    setSelectedTrack(track);
    setShowModal(true);
  };
  return (
    <div className="hone-search-container">
      <div>
        <div className="input-group mt-5 mb-3">
          <input
            type="text"
            className="form-control search-input"
            placeholder="Search for a track"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button
            className="btn btn-primary search-btn"
            onClick={searchTracks}
            disabled={!token}
          >
            <FaSearch /> Search
          </button>
        </div>
        {tracks && tracks.length === 0 ? (
          <div className="no-results-message">
            <p>Your searched song is not found</p>
          </div>
        ) : (
          <div>
            <h2 className="text-dark">Search Results</h2>
            <ul className="list-group">
              {tracks.map((track) => (
                <li key={track.id} className="list-group-item">
                  {!track.album.images.length ? (
                    <span className="track-name-text">{track.name.split(" ")[0]}</span>
                  ) : (
                    <img
                      src={track.album.images.length > 0 ? track.album.images[0].url : ""}
                      alt={track.name}
                      className="track-image"
                    />
                  )}
                  <div className="track-info">
                    {track.name} - {track.artists[0].name}
                  </div>
                  <div className="d-flex flex-column align-items-center">
                    {isFavorite(track) ? (
                      <button
                        className="btn btn-danger"
                        onClick={() => removeFromFavorites(track)}
                      >
                        <FaHeartBroken />
                      </button>
                    ) : (
                      <button
                        className="btn btn-success"
                        onClick={() => !authToken ? alert("Please log in to add to favorites.") : addToFavorites(track)}
                        // onClick={() => addToFavorites(track)}
                      >
                        <FaHeart />
                      </button>
                    )}

                    {/* Add to Playlist Button */}
                    <button
                      className="btn btn-primary ml-2"
                      onClick={() => !authToken ? alert("Please log in to create playlist.") : handleAddToPlaylist(track)}
                      // onClick={() => handleAddToPlaylist(track)}
                    >
                      <FaPlus />
                    </button>
                    {/* Play Button */}
                    <button
                      className="btn btn-warning ml-2"
                      onClick={() => handlePlay(track)}
                    >
                      <FaPlay />
                    </button>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
      {/* Reusable Modal */}
      <AddToPlaylistModal
        showModal={showModal}
        setShowModal={setShowModal}
        selectedTrack={selectedTrack}
        playlists={playlists}
        addToPlaylist={addToPlaylist}
      />
      {/* Player Component */}
      {currentTrack && <Player token={token} track={currentTrack} />}
    </div>
  );
};
