/* eslint-disable no-nested-ternary */
import React from "react";
import WrongFileFormatMessage from "./WrongFileFormatMessage";
import MockModelFileTemplate from "../../../_Debugging/MockModelData/MockModelFileTemplate";
import isDev from "../../../../debugging/DevDetect";

const PreviewBox = ({ fileText, fileIsJSONFormat, fileIsChosen }) => {
  console.log(fileText);

  return (
    <div>
      <div>Preview</div>
      <div className="previewBox">
        {fileIsChosen ? (
          fileIsJSONFormat ? (
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

export default PreviewBox;
