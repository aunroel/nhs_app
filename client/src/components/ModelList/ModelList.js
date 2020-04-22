/* eslint-disable no-restricted-syntax */
import React, { useState, useEffect } from "react";
import axios from "axios";
import { ModelBox } from "./ModelBox";
import "./model-list.css";

const ModelList = () => {
  const [searchValue, setSearchValue] = useState(null);
  const [models, setModels] = useState(null);

  const loadModels = async () => {
    try {
      const res = await axios.get("/api/model/list");
      setModels(JSON.parse(res.data));
      console.log(res);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    loadModels();
  }, []);

  // const modelsToDisplay =

  return (
    <div className="model-list-container">
      <div className="filter-box">
        <input
          type="text"
          placeholder="Search by model name"
          onChange={(event) => setSearchValue(event.target.value)}
        />
      </div>
      {models ? (
        models
          .filter((model) => {
            const { name } = model;
            if (!searchValue) return true;
            const searchItems = searchValue.split(" ");
            for (const string of searchItems) {
              if (name.indexOf(string) !== -1) return true;
            }
            return false;
          })
          .map((model) => {
            return <ModelBox key={model.filename} modelData={model} />;
          })
      ) : (
        <h3>Loading models... </h3>
      )}
    </div>
  );
};

export default ModelList;
