import React, { useState } from "react";
import './AddToPlaylistModal.css'

const AddToPlaylistModal = ({
  showModal,
  setShowModal,
  selectedTrack,
  playlists,
  addToPlaylist
}) => {
  const [newPlaylistName, setNewPlaylistName] = useState("");
  const [selectedPlaylist, setSelectedPlaylist] = useState("");

  const getFirstWord = (trackName) => {
    return trackName.split(" ")[0];
  };

  const handleAddTrackToPlaylist = () => {
    if (selectedPlaylist) {
      addToPlaylist(selectedPlaylist, selectedTrack);
    } else if (newPlaylistName) {
      addToPlaylist(newPlaylistName, selectedTrack);
    }
    setShowModal(false);
    setSelectedPlaylist("");
    setNewPlaylistName("");
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedPlaylist("");
    setNewPlaylistName("");
  };

  return (
    showModal && (
      <div className="modal-overlay">
        <div className="modal-content">
          <h4 className="modal-title">
            Add{" "}
            {selectedTrack?.album.images.length === 0
              ? getFirstWord(selectedTrack.name)
              : selectedTrack?.name}{" "}
            to Playlist
          </h4>
          <div className="modal-body">
            {/* Display image or the first word */}
            <div className="track-image-container">
              {selectedTrack?.album.images.length === 0 ? (
                <span className="track-name-text">
                  {getFirstWord(selectedTrack.name)}
                </span>
              ) : (
                <img
                  src={selectedTrack?.album.images[0]?.url || ""}
                  alt={`${selectedTrack?.name} album cover`}
                  className="track-image"
                />
              )}
            </div>

            {/* Dropdown for existing playlists */}
            <div className="form-group">
              <label>Select Playlist:</label>
              <select
                value={selectedPlaylist}
                onChange={(e) => setSelectedPlaylist(e.target.value)}
                className="form-control"
              >
                <option value="">-- Select Playlist --</option>
                {playlists.map((playlist) => (
                  <option key={playlist.id} value={playlist.name}>
                    {playlist.name}
                  </option>
                ))}
              </select>
            </div>
            {/* Input to create a new playlist */}
            <div className="form-group">
              <label>Create New Playlist:</label>
              <input
                type="text"
                value={newPlaylistName}
                onChange={(e) => setNewPlaylistName(e.target.value)}
                placeholder="New playlist name"
                className="form-control"
              />
            </div>
          </div>
          <div className="modal-footer">
            <button onClick={handleAddTrackToPlaylist} className="btn btn-success">
              Add to Playlist
            </button>
            <button onClick={handleCloseModal} className="btn btn-danger ml-2">
              Cancel
            </button>
          </div>
        </div>
      </div>
    )
  );
};

export default AddToPlaylistModal;
