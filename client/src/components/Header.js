import React, { useState } from "react";
import { Link, useRouteMatch, useLocation } from "react-router-dom";
import isDev from "../debugging/DevDetect";
import "./header.css";
import "../css/sizing.css";
import "../css/colors.css";
import LinkButton from "./Header/LinkButton";

const Logo = () => (
  <div className="header-logo">
    NHS Wellbeing
    <br />
    Predictor
  </div>
);

const Header = () => {
  const { pathname: currentUrl } = useLocation();

  return (
    <div className="header-container">
      <div className="flex-box-vert-align">
        {/* <Logo style={{ flex: 1 }} /> */}
        <div
          className="flex-box-vert-align overflow-y"
          style={{ flex: 4, justifyContent: "space-between" }}
        >
          <div className="flex-box-vert-align">
            <LinkButton
              linkUrl="/modellist"
              buttonText="Model Viewer"
              currentUrl={currentUrl}
            />

            <LinkButton
              linkUrl="/modelupload"
              buttonText="Model Upload"
              currentUrl={currentUrl}
            />
            <LinkButton
              linkUrl="/dataViewer"
              buttonText="Data Viewer"
              currentUrl={currentUrl}
            />
            {/* {isDev() ? (
              <LinkButton
                className="dim"
                linkUrl="/test"
                buttonText="[Dev] Debugging Test"
                currentUrl={currentUrl}
              />
            ) : null} */}
          </div>
          {/* <div
            className="flex-box-vert-align"
            style={{ justifyContent: "flex-end" }}
          >
            <LinkButton
              linkUrl="/login"
              buttonText="Login"
              currentUrl={currentUrl}
            />
            <LinkButton
              linkUrl="/register"
              buttonText="Sign up"
              currentUrl={currentUrl}
            />
          </div> */}
        </div>
      </div>
    </div>
  );
};

Header.propTypes = {};

export default Header;
