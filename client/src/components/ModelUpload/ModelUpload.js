/* eslint-disable no-nested-ternary */
/* eslint-disable react/jsx-boolean-value */
import React, { useState } from "react";
import axios from "axios";
import { UploadButton } from "./subcomponents/UploadButton";
import "./_modelUpload.css";

const uploadStatuses = {
  NONE: "NONE",
  PENDING: "PENDING",
  DONE: "DONE",
  ERROR: "ERROR",
};

const ModelUpload = () => {
  const [fileProperties, setFileProperties] = useState({
    fileIsH5: null,
  });

  const [file, setFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState(uploadStatuses.NONE);

  const handleFiles = async (event) => {
    const f = event.target.files[0];
    setFile(f);
    setUploadStatus(uploadStatuses.NONE);

    console.log(f);
    const isFormatValid = f.name.indexOf(".h5") > -1;

    if (!isFormatValid) {
      setFileProperties({
        fileIsH5: false,
      });
      return;
    }

    setFileProperties({
      fileIsH5: true,
    });
  };

  const fileIsChosen = fileProperties.fileIsH5 != null;
  const fileReadyToUpload = fileProperties.fileIsH5 === true;

  const upload = async (e) => {
    setUploadStatus(uploadStatuses.PENDING);

    e.preventDefault();
    const formData = new FormData();
    formData.append("tf_model", file);

    try {
      const res = await axios.post("/api/model/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      if (res.status === 200) {
        setUploadStatus(uploadStatuses.DONE);
      } else {
        setUploadStatus(uploadStatuses.ERROR);
      }
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div>
      <h2>Model Upload</h2>
      <div style={{ display: "flex" }}>
        <input type="file" id="modelFileUpload" onChange={handleFiles} />
      </div>
      <br />
      {fileIsChosen ? (
        fileProperties.fileIsH5 === true ? (
          <h3>Model is ready to upload</h3>
        ) : (
          <h3 className="red">File with he model must be .h5 format.</h3>
        )
      ) : (
        <h3>Please choose a .h5 file to upload.</h3>
      )}
      <br />
      <UploadButton onClick={upload} enabled={fileReadyToUpload} />
      <br />
      {uploadStatus === uploadStatuses.PENDING ? (
        <h3 className="yellow">Model is being uploaded...</h3>
      ) : null}
      {uploadStatus === uploadStatuses.DONE ? (
        <h3 className="green">Model uploaded successfully</h3>
      ) : null}
      {uploadStatus === uploadStatuses.ERROR ? (
        <h3 className="red">
          An error has occured during the upload. Please try again
        </h3>
      ) : null}
    </div>
  );
};

export default ModelUpload;
