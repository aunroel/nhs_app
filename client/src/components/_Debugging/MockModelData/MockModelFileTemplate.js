import React from "react";
import "./modelUploadDebug.css";

const mockModelTemplate = `
{
  "modelName": string,
  "opitmizer": string,
  "test accuracy": int,
  "comments": "string
}
`;

export const MockModelFileTemplate = () => {
  return (
    <div className="dim">
      [Dev only] Temporary testing format: <pre>{mockModelTemplate}</pre>
    </div>
  );
};
