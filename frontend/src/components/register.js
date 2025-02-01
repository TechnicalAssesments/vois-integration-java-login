import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../api"; 

function Register() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    email: "",
    phone: "",
    firstName: "",
    lastName: "",
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await signup(form); 
      alert(response.message || "Registration Successful"); 
      navigate("/login"); 
    } catch (error) {
      alert(error.message || "Registration Failed");
    }
  };

  return (
    <div className="container">
      <h2 className="title">Register</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Username:</label>
          <input type="text" name="username" onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input type="password" name="password" onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input type="email" name="email" onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Phone:</label>
          <input type="text" name="phone" onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>First Name:</label>
          <input type="text" name="firstName" onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label>Last Name:</label>
          <input type="text" name="lastName" onChange={handleChange} required />
        </div>
        <button type="submit" className="button">
          Register
        </button>
      </form>
      <Link to="/login" className="link">
        Already have an account? Login
      </Link>
    </div>
  );
}

export default Register;