import React from "react";
import { Link } from "react-router-dom";
import "../header.css";

const LinkButton = ({ linkUrl, buttonText, currentUrl }) => {
  const isSelected = linkUrl === currentUrl;
  const buttonClassNames =
    "header-button" + (isSelected ? " header-button-selected" : "");

  return (
    <Link to={linkUrl} className={buttonClassNames}>
      <div>{buttonText}</div>
    </Link>
  );
};

export default LinkButton;
