/* eslint-disable no-nested-ternary */
/* eslint-disable react/jsx-boolean-value */
import React, { useState } from "react";
import axios from "axios";
import { UploadButton } from "./subcomponents/UploadButton";
import "css/dashboard/model-upload.css";
import { Button } from "react-bootstrap";
import fileDownload from "js-file-download";
import { useHistory } from "react-router";

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

  const downloadTemplate = async () => {
    try {
      const res = await axios.get("/api/models/template", {}, {});
      // const file = new File(res.data, "t.zip");
      // ArrayBuffer;
      // fileDownload(new ArrayBuffer(res.data), "tf_template.zip");
      console.log(res);
      if (res.status === 200) {
      } else {
      }
    } catch (err) {
      console.log(err);
    }
  };

  const upload = async (e) => {
    setUploadStatus(uploadStatuses.PENDING);

    e.preventDefault();
    const formData = new FormData();
    formData.append("tf_model", file);

    try {
      const res = await axios.post("/api/models/upload", formData, {
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
      <div style={{ display: "flex", justifyContent: "flex-end" }}>
        {/* <Button
          variant="primary"
          onClick={() => {
            console.log("click");
            downloadTemplate();
          }}
        >
          Download .py template
        </Button> */}
        <a href="http://localhost:5000/api/models/template" download>
          Download .py template
        </a>
      </div>
      <br />
      <br />
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
        <>
          <h3 className="yellow">
            Model is being uploaded and converted to TF Lite...{" "}
          </h3>
          <h5>This may take up to 15 seconds</h5>
        </>
      ) : null}
      {uploadStatus === uploadStatuses.DONE ? (
        <h3 className="green">
          Model uploaded and converted to TF Lite successfully
        </h3>
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
