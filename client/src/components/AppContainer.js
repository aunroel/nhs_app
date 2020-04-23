import React from "react";
import "./Dashboard/DashbarodHeader/node_modules/css/sizing.css";

const flexBox = {
  // position: "absolute",
  display: "flex",
  justifyContent: "center",
  width: "100%",
  minHeight: "100%",
};

const Container = ({ children }) => {
  return (
    <div style={flexBox}>
      <div className="appcontainer content-box">{children}</div>
    </div>
  );
};

Container.propTypes = {};

export default Container;
