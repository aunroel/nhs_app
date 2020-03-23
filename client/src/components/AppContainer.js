import React from "react";
import PropTypes from "prop-types";
import "../css/sizing.css";

const flexBox = {
  position: "absolute",
  display: "flex",
  justifyContent: "center",
  width: "100%",
  minHeight: "100%"
};

const Container = ({ children }) => {
  return (
    <div style={flexBox}>
      <div className="container content-box">{children}</div>
    </div>
  );
};

Container.propTypes = {};

export default Container;
