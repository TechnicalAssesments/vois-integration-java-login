import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../api"; 

function Login() {
  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const navigate = useNavigate();

  const handleChange = (e) => {
    setLoginData({ ...loginData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await login(loginData); 
      alert(response.message || "Login Successful");
      navigate("/welcome"); 
    } catch (error) {
      console.log(error);
      alert(error.message || "Login Failed");
    }
  };

  return (
    <div className="container">
      <h2 className="title">Login</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Username:</label>
          <input type="text" name="username" onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input type="password" name="password" onChange={handleChange} required />
        </div>
        <button type="submit" className="button">
          Login
        </button>
      </form>
      <Link to="/" className="link">
        Create new account?
      </Link>
    </div>
  );
}

export default Login;