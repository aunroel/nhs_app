/* eslint-disable no-restricted-syntax */
import React, { useState } from "react";
import { ModelBox } from "./ModelBox";
import "./model-list.css";

const mockModel = {
  name: "Mock model name",
  uploadDate: "23/03/2020",
  regressionType: "Ridge",
  features: [
    "number of steps",
    "calls per week",
    "post code as world coordinates"
  ],
  trainingAccuracyHistory: [
    0.1,
    0.33,
    0.67,
    0.74,
    0.78,
    0.82,
    0.85,
    0.85,
    0.85
  ],
  trainingLossHistory: [20, 12, 8, 7.5, 7.3, 7.2, 7.15, 7.14, 7.14, 7.14],
  testAccuracy: 0.85,
  comments: [
    "This model performed well",
    "dropping that one feature from learning helped"
  ]
};

const modelNames = ["Mock model 1", "Mock model 2", "Mock model 3"];

const ModelList = () => {
  const [searchValue, setSearchValue] = useState(null);

  return (
    <div className="model-list-container">
      <div className="filter-box">
        <input
          type="text"
          placeholder="Search by model name"
          onChange={event => setSearchValue(event.target.value)}
        />
      </div>
      {modelNames
        .filter(name => {
          if (!searchValue) return true;
          const searchItems = searchValue.split(" ");
          for (const string of searchItems) {
            if (name.indexOf(string) !== -1) return true;
          }
          return false;
        })
        .map(name => {
          const modelData = { ...mockModel, name };
          return <ModelBox key={name} modelData={modelData} />;
        })}
    </div>
  );
};

export default ModelList;
