import React from "react";
import { Link } from "react-router-dom";
import isDev from "../debugging/DevDetect";
import "./header.css";
import "../css/sizing.css";
import "../css/colors.css";

const Logo = () => <div className="header-logo">NHS Wellbeing Predictor</div>;

const Header = () => {
  return (
    <div className="header-container">
      <div className="flex-box-vert-align">
        <Logo style={{ flex: 1 }} />
        <div
          className="flex-box-vert-align overflow-y"
          style={{ flex: 4, justifyContent: "space-between" }}
        >
          <div className="flex-box-vert-align">
            <Link to="/modellist" className="header-button">
              <div>Model Viewer</div>
            </Link>
            <Link to="/modelupload" className="header-button">
              <div>Model Upload</div>
            </Link>
            {isDev() ? (
              <Link to="/test" className="header-button dim">
                [Dev] Debugging Test
              </Link>
            ) : null}
          </div>
          <div
            className="flex-box-vert-align"
            style={{ justifyContent: "flex-end" }}
          >
            <Link to="/login" className="header-button">
              Login
            </Link>
            <Link to="/register" className="header-button">
              Sign up
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

Header.propTypes = {};

export default Header;
