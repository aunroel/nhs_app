import React from "react";

const wrongFormatStyle = {
  color: "crimson"
};

/**
 * TODO
 *  - correct <code> font size
 */

export default () => {
  return (
    <div style={wrongFormatStyle}>
      The uploaded file is of wrong format. Please upload a<code>.json</code>{" "}
      file.
    </div>
  );
};
