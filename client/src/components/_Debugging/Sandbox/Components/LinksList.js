import React from "react";
import { PropTypes } from "prop-types";
import { Link } from "react-router-dom";
import SubURL from "../Classes/SubURL";

const LinksList = ({ url, subUrlPaths }) => {
  return (
    <>
      <h2>Topics</h2>
      <ul>
        {subUrlPaths.map(({ subURL, subPageName }) => (
          <li key={subURL}>
            <Link to={`${url}/${subURL}`}>{subPageName}</Link>
          </li>
        ))}
      </ul>
    </>
  );
};

LinksList.propTypes = {
  url: PropTypes.string.isRequired,
  subUrlPaths: PropTypes.arrayOf(PropTypes.instanceOf(SubURL)).isRequired
};

export default LinksList;
