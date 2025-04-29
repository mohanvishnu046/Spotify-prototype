import React from "react";
import { FaHeartBroken } from "react-icons/fa";

export const Favorites = ({ favorites, removeFromFavorites }) => (
  <div>
    <h2 className="text-dark mt-5 ">Favorites</h2>
    {favorites.length === 0 ? (
      <p>No favorites added yet.</p>
    ) : (
      <ul className="list-group">
        {favorites.map((track, index) => (
          <li key={index} className="list-group-item d-flex justify-content-between align-items-center">
            {/* Check if album image exists */}
            {track.album.images.length ? (
              <img src={track.album.images[0].url} alt={track.name} className="track-image" />
            ) : (
              <span className="track-name-text">{track.name.split(" ")[0]}</span>
            )}
            <div className="track-info">
              {track.name} - {track.artists[0].name}
            </div>
            {/* Remove from favorites button */}
            <button
              className="btn btn-danger"
              onClick={() => removeFromFavorites(track)}
            >
              <FaHeartBroken />
            </button>
          </li>
        ))}
      </ul>
    )}
  </div>
);

