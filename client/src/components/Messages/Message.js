import React from "react";
import PropTypes from "prop-types";

const Message = ({ msgText, bootstrapColor }) => {
  return <div></div>;
};

Message.propTypes = {
  msgText: PropTypes.string.isRequired,
  bootstrapColor: PropTypes.string.isRequired,
};

export default Message;
