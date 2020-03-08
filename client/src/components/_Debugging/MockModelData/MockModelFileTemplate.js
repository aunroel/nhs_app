import React from "react";

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
    <div>
      Temporary testing format:
      <pre>{mockModelTemplate}</pre>
    </div>
  );
};
