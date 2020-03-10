import React, { useState } from "react";

const previewBox = {
  width: "100%",
  height: "20em",
  borderWidth: 2,
  borderColor: "black",
  borderStyle: "solid",
  padding: "1em",
  overflow: "auto"
};

const wrongFormatStyle = {
  color: "crimson"
};

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
          isUploadedFileJSONFormat === true ? (
            <pre>{fileText}</pre>
          ) : (
            <div style={wrongFormatStyle}>
              The uploaded file is of wrong format. Please upload a
              <code>.json</code> file.
            </div>
          )
        ) : null}
      </div>
      <br />
      <div>Confirm upload here</div>
    </div>
  );
};
