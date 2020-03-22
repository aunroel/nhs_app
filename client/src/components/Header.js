import React from "react";
import { Link } from "react-router-dom";
import "./main-page-styles.css";

const Header = () => {
  return (
    <div>
      <h1>NHS Wellbeing Predictor</h1>
      <div style={{ display: "flex" }}>
        <Link to="/modellist" className="header-button-style">
          <div>Model Viewer</div>
        </Link>
        <Link to="modelupload" className="header-button-style">
          <div>Model Upload</div>
        </Link>
      </div>
    </div>
  );
};

Header.propTypes = {};

export default Header;
