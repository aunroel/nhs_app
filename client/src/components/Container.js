import React from "react";
import PropTypes from "prop-types";

const flexBox = {
  position: "absolute",
  display: "flex",
  justifyContent: "center",
  height: "100%",
  width: "100%",
  margin: 0,
  padding: 0
};

const container = {
  width: "60em",
  backgroundColor: "white",
  padding: "1em"
};

const Container = ({ children }) => {
  return (
    <div style={flexBox}>
      <div style={container}>{children}</div>
    </div>
  );
};

Container.propTypes = {};

export default Container;
