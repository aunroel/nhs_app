import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

const headerButtonStyle = {
  width: "10em",
  height: "2em",
  borderStyle: "solid",
  borderWidth: 0,
  borderBottomWidth: 2,
  margin: "0.2em",
  textDecoration: "none",
  color: "black"
};

const Header = props => {
  return (
    <div>
      <h1>NHS Wellbeing Predictor</h1>
      <div style={{ display: "flex" }}>
        <Link to="/modellist" style={headerButtonStyle}>
          <div>Model Viewer</div>
        </Link>
        <Link to="modelupload" style={headerButtonStyle}>
          <div>Model Upload</div>
        </Link>
      </div>
    </div>
  );
};

Header.propTypes = {};

export default Header;
