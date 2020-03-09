import React from "react";
import WrongFileFormatMessage from "./WrongFileFormatMessage";
import { MockModelFileTemplate } from "../_Debugging/MockModelData/MockModelFileTemplate";

export const previewBox = () => {
  return (
    <div>
      <div>Preview</div>
      <div className="previewBox">
        {fileIsChosen ? (
          isUploadedFileJSONFormat ? (
            <pre>{fileText}</pre>
          ) : (
            <WrongFileFormatMessage />
          )
        ) : isDev() ? (
          <MockModelFileTemplate />
        ) : null}
      </div>
    </div>
  );
};
