import React from "react";
import "../../../css/colors.css";

const mockModelTemplate = `
{
  "modelName": string,
  "opitmizer": string,
  "test accuracy": int,
  "comments": "string
}
`;

const MockModelFileTemplate = () => {
  return (
    <div className="dim">
      [Dev only] Temporary testing format:
      <pre>{mockModelTemplate}</pre>
    </div>
  );
};

export default MockModelFileTemplate;
