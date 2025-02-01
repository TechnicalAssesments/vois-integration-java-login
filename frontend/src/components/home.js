import React from "react";
import { Link } from "react-router-dom";


function Welcome() {
    return (
      <div className="container">
        <h2 className="title">Welcome!</h2>
        <p className="message">You have successfully logged in.</p>
        <Link to="/login" className="link">Logout</Link>
      </div>
    );
  }

export default Welcome;