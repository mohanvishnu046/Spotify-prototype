import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route,Navigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import { Home } from "./pages/Home";
import { Search } from "./pages/Search";
import { Favorites } from "./pages/Favorites";
import { Playlist } from "./pages/Playlist";
import { About } from "./pages/About";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Navbar } from "./components/Navbar";
import "./App.css";
import "@mui/material/styles";
import ProtectedRoute from "./components/ProtectedRoute";

const CLIENT_ID = "ebca226ecc384cd8bc2f26a3a58b065d";
const CLIENT_SECRET = "4fda45163ac44576be68b8080bb8499d";
const API_URL = "http://localhost:5000";  // Base URL for JSON server

const App = () => {
  const [token, setToken] = useState("");
  const [tracks, setTracks] = useState([]);
  const [favorites, setFavorites] = useState([]);
  const [playlists, setPlaylists] = useState([]);
  const [authToken,setAuthToken] =useState(localStorage.getItem("authToken") || null);
  const [userId,setUserId]=useState(localStorage.getItem("userId") || null);
  useEffect(() => {
    const fetchToken = async () => {
      const response = await fetch("https://accounts.spotify.com/api/token", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          Authorization: "Basic " + btoa(CLIENT_ID + ":" + CLIENT_SECRET),
        },
        body: new URLSearchParams({ grant_type: "client_credentials" }),
      });
      
      const data = await response.json();
      if (data.access_token) {
        setToken(data.access_token);
      } else {
        console.error("Failed to get token:", data);
      }
    };

    fetchToken();
  }, []);

  // Fetch data from JSON Server (replace useState logic)
  useEffect(() => {
    if (authToken) {  
     fetchFavorites();
     fetchPlaylists();
    }
  }, []);
  const fetchFavorites = async () => {
    // const response = await fetch(`${API_URL}/favorites`);
    try{
      const response = await fetch(`http://localhost:8050/api/v1/list/favorites/${userId}`,{
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${authToken}`,
          'Content-Type': 'application/json',
        },
      });
      const data = await response.json();
      setFavorites(data);
    }catch(error){
      console.error("PlayList API is down....");
    }
  };
  const fetchPlaylists = async () => {
    // const response = await fetch(`${API_URL}/playlists`);
    try{
    const response = await fetch(`http://localhost:8050/api/v1/list/playlists?userId=${userId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${authToken}`,
        'Content-Type': 'application/json',
      },
    });
    
    const data = await response.json();
    setPlaylists(data);
    
    }catch(error){
      console.error("PlayList API is down....");
    }
  };
  // Add to favorites via API
  const addToFavorites = async (track) => {
    if (favorites.some((favoriteTrack) => favoriteTrack.id === track.id)) {
      console.log("Track is already in favorites!");
      return;
    }
    // const response = await fetch(`${API_URL}/favorites`, {
      const response = await fetch(`http://localhost:8050/api/v1/list/favorites?userId=${userId}`, {
      method: "POST",
      headers: {
        'Authorization': `Bearer ${authToken}`,
        "Content-Type": "application/json" 
      },
      body: JSON.stringify(track),
    });
    const newFavorite = await response.json();
    setFavorites((prevFavorites) => [...prevFavorites, newFavorite]);
  };

  // Remove from favorites via API
  const removeFromFavorites = async (trackToRemove) => {
    await fetch(`http://localhost:8050/api/v1/list/favorites/${userId}/${trackToRemove.id}`, {
    // await fetch(`${API_URL}/favorites/${trackToRemove.id}`, {
      method: "DELETE",
      headers: {
        'Authorization': `Bearer ${authToken}`,
        "Content-Type": "application/json" 
      },
    });
    setFavorites(favorites.filter((track) => track.id !== trackToRemove.id));
  };
  
  const addToPlaylist = async (playlistNameOrId, track) => {
    try {
      // Find the existing playlist
      const existingPlaylist = playlists.find(
        (playlist) => playlist.name === playlistNameOrId || playlist.id === playlistNameOrId
      );
  
      if (existingPlaylist) {
        // Check if the track is already in the playlist
        const isTrackInPlaylist = existingPlaylist.tracks.some(
          (existingTrack) => existingTrack.id === track.id
        );
  
        if (isTrackInPlaylist) {
          console.log("Track is already in the playlist!");
          return;
        }
  
        // Add track to existing playlist via API
        await fetch(`http://localhost:8050/api/v1/list/playlists/${existingPlaylist.id}/tracks`, {
          method: "POST",
          headers: {
            'Authorization': `Bearer ${authToken}`,
            "Content-Type": "application/json"
          },
          body: JSON.stringify(track),
        });
  
        // Refresh the playlist to get updated data
        fetchPlaylists(userId);
      } else {
        // Create new playlist with the track
        // Generating a simple ID
        const newPlaylistId = `playlist_${Date.now()}`;
        
        await fetch(`http://localhost:8050/api/v1/list/playlists?playlistId=${newPlaylistId}&playlistName=${playlistNameOrId}&userId=${userId}`, {
          method: "POST",
          headers: {
            'Authorization': `Bearer ${authToken}`,
            "Content-Type": "application/json"
          },
        });
  
        // Then add the track to the newly created playlist
        await fetch(`http://localhost:8050/api/v1/list/playlists/${newPlaylistId}/tracks`, {
          method: "POST",
          headers: { 
            'Authorization': `Bearer ${authToken}`,
            "Content-Type": "application/json" 
          },
          body: JSON.stringify(track),
        });
  
        // Refresh playlists to get the new one
        fetchPlaylists(userId);
      }
    } catch (error) {
      console.error('Error adding to playlist:', error);
    }
  };

  // Remove from playlist via API
  const removeFromPlaylist = async (playlistId, trackId) => {
    const playlistToUpdate = playlists.find(
      (playlist) => playlist.id === playlistId
    );
    const updatedPlaylist = {
      ...playlistToUpdate,
      tracks: playlistToUpdate.tracks.filter((track) => track.id !== trackId),
    };
      await fetch(`http://localhost:8050/api/v1/list/playlists/${playlistId}/tracks/${trackId}`, {
      method: "DELETE",
    });
    setPlaylists((prevPlaylists) =>
      prevPlaylists.map((playlist) =>
        playlist.id === playlistId ? updatedPlaylist : playlist
      )
    );
  };

  return (
    <Router>
      <div className="container mt-4 app-background">
        {/* <Navbar /> */}
        <Navbar
          token={authToken} 
          setToken={setAuthToken} 
          setUserId={setUserId}
        />
        <Routes>
          <Route element={<ProtectedRoute/>}>
            <Route
              path="/"
              element={
                <Home
                  token={token}
                  addToFavorites={addToFavorites}
                  removeFromFavorites={removeFromFavorites}
                  favorites={favorites}
                  playlists={playlists}
                  addToPlaylist={addToPlaylist}
                />
              }
            />

            <Route
              path="/search"
              element={
                <Search
                  token={token}
                  tracks={tracks}
                  setTracks={setTracks}
                  addToFavorites={addToFavorites}
                  removeFromFavorites={removeFromFavorites}
                  favorites={favorites}
                  playlists={playlists}
                  addToPlaylist={addToPlaylist}
                />
              }
            />

            <Route
              path="/favorites"
              element={
                authToken ? (<Favorites favorites={favorites} removeFromFavorites={removeFromFavorites}/>
                ): (
                  <Navigate to="/login" replace />
                )}
            />

            <Route
              path="/playlist"
              element={
                authToken ? (<Playlist existingPlaylists={playlists} removeFromPlaylist={removeFromPlaylist}/>
                ): (
                  <Navigate to="/login" replace />
                )}
            />
          </Route>
          <Route path="/about" element={<About />} />
          <Route path="/login" element={<Login setToken={setAuthToken} setUserId={setUserId}/>} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
