import React from "react";
import { Link } from "react-router-dom";
import isDev from "../debugging/DevDetect";
import "./_header.css";
import "../css/colors.css";

const Header = () => {
  return (
    <div className="headerStyle">
      <h1>NHS Wellbeing Predictor</h1>
      <div style={{ display: "flex" }}>
        <Link to="/modellist" className="headerButtonStyle">
          <div>Model Viewer</div>
        </Link>
        <Link to="/modelupload" className="headerButtonStyle">
          <div>Model Upload</div>
        </Link>
        {isDev() ? (
          <Link to="/test" className="headerButtonStyle dim">
            [Dev] Debugging Test
          </Link>
        ) : null}
      </div>
    </div>
  );
};

Header.propTypes = {};

export default Header;
