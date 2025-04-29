import React, { useState, useEffect } from "react";
import { FaHeart, FaHeartBroken, FaPlus,FaPlay} from "react-icons/fa";
import AddToPlaylistModal from "../components/AddToPlaylistModal";
import Player from "../components/Player";
export const Home = ({ token, addToFavorites, removeFromFavorites, favorites, playlists, addToPlaylist }) => {
  const [tracks, setTracks] = useState([]);
  const [searchTerm, setSearchTerm] = useState("telugu");
  const [showModal, setShowModal] = useState(false);
  const [selectedTrack, setSelectedTrack] = useState(null);
  const [currentTrack, setCurrentTrack] = useState(null);
  const authToken = localStorage.getItem("authToken") || null;

  const isFavorite = (track) => {
    return favorites.some((favoriteTrack) => favoriteTrack.id === track.id);
  };
  const handlePlay = (track) => {
    setCurrentTrack(track); // Set the track to be played
  };
  const fetchTracks = async () => {
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
      setTracks(data.tracks.items || []);
    } catch (error) {
      console.error("Error fetching tracks:", error);
    }
  };

  useEffect(() => {
    if (token) {
      fetchTracks();
    }
  }, [token]);

  const handleAddToPlaylist = (track) => {
    setSelectedTrack(track);
    setShowModal(true);
  };

  return (
    <div className="hone-search-container">
      <h2 className="text-dark mt-5">Welcome to the Music App</h2>

      {/* Display search results */}
      <div>
        {tracks.length === 0 ? (
          <div className="no-results-message">
            <p>Your searched song is not found</p>
          </div>
        ) : (
          <div>
            <ul className="list-group">
              {tracks.map((track) => (
                <li key={track.id} className="list-group-item">
                  {/* Show track name if no image exists */}
                  {!track.album.images.length ? (
                    <span className="track-name-text">{track.name.split(" ")[0]}</span>
                  ) : (
                    <img
                      src={track.album.images[0]?.url || ""}
                      alt={`${track.name} album cover`}
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
