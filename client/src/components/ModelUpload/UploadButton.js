import React from "react";

export const UploadButton = ({ onClick, enabled }) => {
  return (
    <button onClick={onClick} disabled={!enabled}>
      Upload model
    </button>
  );
};
