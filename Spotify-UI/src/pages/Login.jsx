import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./login.css";
export const Login = ({ setToken, setUserId, setIsLoggedIn}) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      setErrorMessage("Email and password are required.");
      return;
    }

    try {
      console.log(email,"  :: ",password);
      const response = await fetch("http://localhost:8050/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (data.token) {
        console.log("logintoken",data.token)
        setToken(data.token); // Store token in state and localStorage
        setUserId(data.userId);//to store userid
        localStorage.setItem("authToken", data.token);
        localStorage.setItem("userId",data.userId);
        console.log(`token :: ${data.token} and userID :${data.userId}`)
        navigate("/"); // Redirect to Home page
      } else {
        setErrorMessage(data.message || "Login failed.");
      }
    } catch (error) {
      setErrorMessage("Error occurred. Please try again.");
    }
  };

  return (
    <div className="login-container">
  <div className="login-card">
    <h2 className="login-header">Login</h2>
    {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
    <form onSubmit={handleLogin}>
      <div className="form-group">
        <label htmlFor="email">Email</label>
        <input
          type="email"
          className="form-control"
          id="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Enter your email"
        />
      </div>
      <div className="form-group">
        <label htmlFor="password">Password</label>
        <input
          type="password"
          className="form-control"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Enter your password"
        />
      </div>
      <button type="submit" className="btn btn-primary login-btn">
        Login
      </button>
    </form>
  </div>
</div>

  );
};
