import React from "react";

export const ChooseFileButton = ({ onChoice }) => {
  return (
    <div style={{ display: "flex" }}>
      <input type="file" id="modelFileUpload" onChange={onChoice} />
    </div>
  );
};
