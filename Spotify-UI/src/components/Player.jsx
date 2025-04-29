import React from "react";
import "./Player.css"

const Player = ({ track }) => {
  if (!track) {
    return (
      <div className="player">
        <h3>No track selected</h3>
      </div>
    );
  }

  return (
    <div className="player">
      <div className="track-details">
        {/* Album Art */}
        <img
          src={track.album.images[0]?.url} // Use the first image in the album's images array
          alt={track.name}
          className="album-art"
        />
        {/* Track Name and Artist */}
        <div className="track-info">
          <h4>{track.name}</h4>
          <p>{track.artists.map((artist) => artist.name).join(", ")}</p>
        </div>
      </div>
    </div>
  );
};

export default Player;