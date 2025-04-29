import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export const Register = ({ setToken }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();

    if (!email || !password || !confirmPassword) {
      setErrorMessage("All fields are required.");
      return;
    }

    if (password !== confirmPassword) {
      setErrorMessage("Passwords do not match.");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8050/api/v1/user/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });
      const data = response.status === 409 
        ? await response.text() 
        : await response.json();
      if (data.userId) {
        if (data.token) {
          setToken(data.token);
          localStorage.setItem("authToken", data.token);
          localStorage.setItem("userId", data.userId);
          console.log(`Registration succeeded token :: ${data.token} and userID: ${data.userId}`);
          navigate("/");
        }
        alert("Registration successful! Redirecting...");
        navigate("/login");
      } else if (response.status === 409) {
        setErrorMessage(data|| "User already exists with this email.");
      }else {
        setErrorMessage(data.message || "Registration failed. Please try again.");
      }
    } catch (error) {
      setErrorMessage("Error occurred. Please try again.");
      console.error("Registration error:", error);
    }
  };

  return (
    <div className="login-container">
  <div className="login-card">
    <h2 className="login-header">Register</h2>
    {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
    <form onSubmit={handleRegister}>
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
      <div className="form-group">
        <label htmlFor="confirmPassword">Confirm Password</label>
        <input
          type="password"
          className="form-control"
          id="confirmPassword"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          placeholder="Confirm your password"
        />
      </div>
      <button type="submit" className="btn btn-primary login-btn">
        Register
      </button>
    </form>
  </div>
</div>

  );
};