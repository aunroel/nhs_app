import React, { useState } from "react";
import WrongFileFormatMessage from "./WrongFileFormatMessage";
import isDev from "../../debugging/DevDetect";
import { MockModelFileTemplate } from "../_Debugging/MockModelData/MockModelFileTemplate";
import { UploadButton } from "./UploadButton";

const previewBox = {
  width: "100%",
  height: "20em",
  borderWidth: 2,
  borderColor: "black",
  borderStyle: "solid",
  padding: "1em",
  overflow: "auto"
};

const FileText = fileText => <pre>{fileText}</pre>;

export const ModelUpload = () => {
  const [fileText, setFileText] = useState(null);
  const [isUploadedFileJSONFormat, setFormatValid] = useState(null);

  const handleFiles = async event => {
    const file = event.target.files[0];

    const isFormatValid = file.type === "application/json";
    setFormatValid(isFormatValid);

    if (!isFormatValid) return;

    const jsonFile = JSON.constructor(file);
    const jsonAsString = await jsonFile.text();
    setFileText(jsonAsString);
  };

  const fileIsUploaded = isUploadedFileJSONFormat != null;
  const fileReadyToUpload = fileIsUploaded && isUploadedFileJSONFormat;

  const upload = () => {};

  return (
    <div>
      <h2>Model Upload</h2>
      <div style={{ display: "flex" }}>
        <input
          type="file"
          id="modelFileUpload"
          accept=".json"
          onChange={handleFiles}
        />
      </div>
      <br />
      <div>Preview</div>
      <div style={previewBox}>
        {fileIsUploaded ? (
          isUploadedFileJSONFormat ? (
            <pre>{fileText}</pre>
          ) : (
            <WrongFileFormatMessage />
          )
        ) : isDev() ? (
          <MockModelFileTemplate />
        ) : null}
      </div>
      <br />
      <UploadButton onClick={upload} enabled={fileReadyToUpload} />
    </div>
  );
};
