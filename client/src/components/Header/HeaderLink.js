import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

const HeaderLink = ({ to, text }) => {
  return (
    <Link to={to} className="link">
      {text}
    </Link>
  );
};

HeaderLink.propTypes = {
  to: PropTypes.string.isRequired,
  text: PropTypes.string.isRequired,
};

export default HeaderLink;
