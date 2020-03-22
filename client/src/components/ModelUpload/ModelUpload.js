import React, { useState } from "react";
import isDev from "../../debugging/DevDetect";
import WrongFileFormatMessage from "./WrongFileFormatMessage";
import { MockModelFileTemplate } from "../_Debugging/MockModelData/MockModelFileTemplate";
import { UploadButton } from "./UploadButton";
import { ChooseFileButton } from "./ChooseFileButton";
import "./_modelUpload.css";

/**
 * TODO
 *  - remove the temporary testing format and put in
 *    actual TF/PyT model
 *  - add button to download .py input/output template
 *    (with a possiblity to upload trained model parameters)
 *    and a script enabling to download data locally
 *
 *  - add server side model file parsing validation
 *  -
 */

const ModelUpload = () => {
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

  const fileIsChosen = isUploadedFileJSONFormat != null;
  const fileIsReadyToUpload = fileIsChosen && isUploadedFileJSONFormat;

  const upload = () => {};

  return (
    <div>
      <h2>Model Upload</h2>
      <ChooseFileButton onChoice={handleFiles} />
      <br />
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
      <br />
      <UploadButton onClick={upload} enabled={fileIsReadyToUpload} />
    </div>
  );
};

export default ModelUpload;
