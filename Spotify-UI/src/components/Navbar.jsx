import React, { useState } from "react";
import { Link,useNavigate } from "react-router-dom";
import { FaSearch, FaHeart, FaHome, FaBars, FaTimes, FaInfoCircle, FaUser, FaUserPlus, FaMusic, FaSignOutAlt } from "react-icons/fa";
import "./Navbar.css";

export const Navbar = ({ token, setToken, setUserId}) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const handleLogout = () => {
    setToken(null);
    setUserId(null);
    localStorage.removeItem("authToken");
    localStorage.removeItem("userId");
    setMenuOpen(false);
    navigate("/login");
    window.location.reload();
  };

  return (
    <nav className="nav">
      <div className="nav-container">
        <Link className="logo" to="/">
          SpotifyApp
        </Link>
        <button className="menu-toggle" onClick={toggleMenu}>
          {menuOpen ? <FaTimes /> : <FaBars />}
        </button>
        <div className={`nav-links ${menuOpen ? "open" : ""}`}>
          <Link className="nav-link" to="/" onClick={() => setMenuOpen(false)}>
            <FaHome /> Home
          </Link>
          <Link className="nav-link" to="/search" onClick={() => setMenuOpen(false)}>
            <FaSearch /> Search
          </Link>
          <Link className="nav-link" to="/favorites" onClick={() => setMenuOpen(false)}>
            <FaHeart /> Favorites
          </Link>
          <Link className="nav-link" to="/playlist" onClick={() => setMenuOpen(false)}>
            <FaMusic /> Playlist
          </Link>
          {!token ? (
            <>
              <Link className="nav-link" to="/login" onClick={() => setMenuOpen(false)}>
                <FaUser /> Login
              </Link>
              <Link className="nav-link" to="/register" onClick={() => setMenuOpen(false)}>
                <FaUserPlus /> Register
              </Link>
            </>
          ) : (
            <button 
              className="nav-link" 
              onClick={handleLogout}
              style={{ background: 'none', border: 'none', cursor: 'pointer' }}
            >
              <FaSignOutAlt /> Logout
            </button>
          )}
          <Link className="nav-link" to="/about" onClick={() => setMenuOpen(false)}>
            <FaInfoCircle /> About
          </Link>
        </div>
      </div>
    </nav>
  );
};