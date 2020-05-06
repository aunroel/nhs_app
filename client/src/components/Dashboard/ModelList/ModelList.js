/* eslint-disable no-restricted-syntax */
import React, { useState, useEffect } from "react";
import axios from "axios";
import { ModelBox } from "./ModelBox";
import "css/dashboard/model-list.css";

const ModelList = () => {
  const [searchValue, setSearchValue] = useState(null);
  const [models, setModels] = useState(null);

  const loadModels = async () => {
    try {
      const res = await axios.get("/api/models/list");
      console.log("resr", res);
      const parsed = res.data
        .filter((modelMeta) => {
          const json_summary = JSON.parse(modelMeta.json_summary);
          return json_summary.summary;
          // typeof modelMeta.json_summary !== "string"
        })
        .map((modelMeta) => {
          const json_summary = JSON.parse(modelMeta.json_summary);

          return {
            ...modelMeta,
            json_summary: {
              ...json_summary,
              summary: JSON.parse(json_summary.summary),
            },
          };
        });

      console.log("parsed", parsed);
      setModels(parsed);
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
          .filter(({ filename: name }) => {
            if (!searchValue) return true;
            const searchItems = searchValue.split(" ");
            for (const string of searchItems) {
              if (name.indexOf(string) !== -1) return true;
            }
            return false;
          })
          .map((model) => {
            const val_loss = model.json_summary.history.history.val_loss;
            return (
              <ModelBox
                key={model.filename}
                modelName={model.filename}
                validationLossHistory={val_loss}
                validationLoss={val_loss[val_loss.length - 1]}
                optimizerName={model.json_summary.optimizer.name}
                learningRate={model.json_summary.optimizer.learning_rate}
                activation={
                  model.json_summary.summary.config.layers[1].config.activation
                }
              />
            );
          })
      ) : (
        <h3>Loading models... </h3>
      )}
    </div>
  );
};

export default ModelList;
