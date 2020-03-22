import React, { useState } from "react";
import { UploadButton } from "./subcomponents/UploadButton";
import { ChooseFileButton } from "./subcomponents/ChooseFileButton";
import "./_modelUpload.css";
import PreviewBox from "./subcomponents/PreviewBox";

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
  const [fileProperties, setFileProperties] = useState({
    fileText: null,
    fileIsJSONFormat: null
  });

  const handleFiles = async event => {
    const file = event.target.files[0];

    const isFormatValid = file.type === "application/json";

    if (!isFormatValid) {
      setFileProperties({
        fileText: null,
        fileIsJSONFormat: false
      });
      return;
    }

    const jsonFile = JSON.constructor(file);
    const jsonAsString = await jsonFile.text();

    setFileProperties({
      fileText: jsonAsString,
      fileIsJSONFormat: true
    });
  };

  const fileIsChosen = fileProperties.fileIsJSONFormat != null;
  const fileIsReadyToUpload = fileIsChosen && fileProperties.fileIsJSONFormat;

  const upload = () => {};

  const previewBoxProps = { ...fileProperties, fileIsChosen };

  return (
    <div>
      <h2>Model Upload</h2>
      <ChooseFileButton onChoice={handleFiles} />
      <br />
      <PreviewBox {...previewBoxProps} />
      <br />
      <UploadButton onClick={upload} enabled={fileIsReadyToUpload} />
    </div>
  );
};

export default ModelUpload;
